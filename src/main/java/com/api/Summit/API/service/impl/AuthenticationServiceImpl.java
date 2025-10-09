package com.api.Summit.API.service.impl;

import com.api.Summit.API.model.entities.Negocio;
import com.api.Summit.API.model.entities.Role;
import com.api.Summit.API.model.entities.Token;
import com.api.Summit.API.model.entities.User;
import com.api.Summit.API.model.repository.NegocioRepository;
import com.api.Summit.API.model.repository.RoleRepository;
import com.api.Summit.API.model.repository.TokenRepository;
import com.api.Summit.API.model.repository.UserRepository;
import com.api.Summit.API.security.jwt.JwtService;
import com.api.Summit.API.service.interfaces.AuthenticationService;
import com.api.Summit.API.view.dto.AuthenticationRequest;
import com.api.Summit.API.view.dto.AuthenticationResponse;
import com.api.Summit.API.view.dto.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final NegocioRepository negocioRepository;

    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        // Fetch or create USER role
        Role userRole = roleRepository.findByName("USER")
                .orElseGet(() -> {
                    Role newRole = Role.builder().name("USER").permissions(new HashSet<>()).build();
                    return roleRepository.save(newRole);
                });

        var user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(new HashSet<>() {{
                    add(userRole);
                }})
                .build();

        // Associate negocios if provided
        if (request.getNegocioIds() != null && !request.getNegocioIds().isEmpty()) {
            Set<Negocio> negocios = request.getNegocioIds().stream()
                    .map(id -> negocioRepository.findById(id)
                            .orElseThrow(() -> new NoSuchElementException("Negocio not found with id: " + id)))
                    .collect(Collectors.toSet());
            user.setNegocios(negocios);
        }

        var savedUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        saveUserToken(savedUser, jwtToken);
        return AuthenticationResponse.fromUser(savedUser, jwtToken, "Registration successful");
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        revokeAllUserTokens(user);
        var jwtToken = jwtService.generateToken(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.fromUser(user, jwtToken, "Login successful");
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    // Metodo para ver los negocios disponibles
    public Set<Negocio> getNegociosDisponibles() {
        return new HashSet<>(negocioRepository.findAll());
    }
}
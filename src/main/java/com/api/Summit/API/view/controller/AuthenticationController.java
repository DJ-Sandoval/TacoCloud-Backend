package com.api.Summit.API.view.controller;

import com.api.Summit.API.model.entities.Negocio;
import com.api.Summit.API.model.repository.NegocioRepository;
import com.api.Summit.API.model.repository.TokenRepository;
import com.api.Summit.API.service.impl.AuthenticationServiceImpl;
import com.api.Summit.API.service.interfaces.AuthenticationService;
import com.api.Summit.API.view.dto.AuthenticationRequest;
import com.api.Summit.API.view.dto.AuthenticationResponse;
import com.api.Summit.API.view.dto.NegocioDTO;
import com.api.Summit.API.view.dto.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final TokenRepository tokenRepository;
    private final NegocioRepository negocioRepository;
    private final AuthenticationServiceImpl authenticationServiceImpl; // ✅ Agregar

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }


    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("No token provided");
        }
        final String jwt = authHeader.substring(7);
        var storedToken = tokenRepository.findByToken(jwt).orElse(null);
        if (storedToken != null) {
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            tokenRepository.save(storedToken);
            return ResponseEntity.ok("Logout successful");
        }
        return ResponseEntity.badRequest().body("Invalid token");
    }

    // Endpoint para obtener todos los negocios disponibles (para el formulario de registro)
    @GetMapping("/negocios-disponibles")
    public ResponseEntity<Set<NegocioDTO>> getNegociosDisponibles() {
        Set<Negocio> negocios = authenticationServiceImpl.getNegociosDisponibles();
        Set<NegocioDTO> negociosDTO = negocios.stream()
                .map(NegocioDTO::fromNegocio)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(negociosDTO);
    }
}
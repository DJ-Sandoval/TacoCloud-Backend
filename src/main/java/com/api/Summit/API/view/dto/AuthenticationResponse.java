package com.api.Summit.API.view.dto;

import lombok.*;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationResponse {
    private Long userId;
    private String username;
    private String token;
    private String message;
    private Set<NegocioDTO> negocios; // Added field for businesses

    // Helper method to build response with negocios
    public static AuthenticationResponse fromUser(com.api.Summit.API.model.entities.User user, String token, String message) {
        return AuthenticationResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .token(token)
                .message(message)
                .negocios(user.getNegocios().stream()
                        .map(NegocioDTO::fromNegocio)
                        .collect(Collectors.toSet()))
                .build();
    }
}

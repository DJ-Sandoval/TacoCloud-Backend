package com.api.Summit.API.view.dto;

import lombok.*;
import java.util.Set;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserRequest {
    private String username;
    private String password;
    private Set<String> roles;
}

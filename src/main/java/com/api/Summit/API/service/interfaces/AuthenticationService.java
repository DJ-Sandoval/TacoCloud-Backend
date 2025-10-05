package com.api.Summit.API.service.interfaces;

import com.api.Summit.API.view.dto.AuthenticationRequest;
import com.api.Summit.API.view.dto.AuthenticationResponse;
import com.api.Summit.API.view.dto.RegisterRequest;

public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest request);
    AuthenticationResponse authenticate(AuthenticationRequest request);
}

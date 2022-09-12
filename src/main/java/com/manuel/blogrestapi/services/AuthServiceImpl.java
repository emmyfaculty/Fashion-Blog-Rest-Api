package com.manuel.blogrestapi.services;

import com.manuel.blogrestapi.models.JWTAuthResponse;
import com.manuel.blogrestapi.dto.SignInDTO;
import com.manuel.blogrestapi.security.JWTTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final JWTTokenProvider tokenProvider;

    private final AuthenticationManager authenticationManager;


    public AuthServiceImpl(JWTTokenProvider tokenProvider, AuthenticationManager authenticationManager) {
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public JWTAuthResponse createToken(SignInDTO signInDto) {
        String token = generateToken(signInDto);
        return new JWTAuthResponse(token);
    }

    private String generateToken(SignInDTO signInDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                signInDto.getUsernameOrEmail(),
                signInDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProvider.generateToken(authentication);

        return token;
    }

}
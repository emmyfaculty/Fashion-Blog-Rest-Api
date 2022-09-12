package com.manuel.blogrestapi.services;

import com.manuel.blogrestapi.models.JWTAuthResponse;
import com.manuel.blogrestapi.dto.SignInDTO;

public interface AuthService {

    JWTAuthResponse createToken(SignInDTO signInDto);

}
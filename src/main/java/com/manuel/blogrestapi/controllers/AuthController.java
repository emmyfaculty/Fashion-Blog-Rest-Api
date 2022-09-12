package com.manuel.blogrestapi.controllers;

import com.manuel.blogrestapi.models.JWTAuthResponse;
import com.manuel.blogrestapi.dto.SignInDTO;
import com.manuel.blogrestapi.dto.SignUpDTO;
import com.manuel.blogrestapi.services.AuthService;
import com.manuel.blogrestapi.services.AuthServiceImpl;
import com.manuel.blogrestapi.services.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Api(value = "Auth controller exposes signing and signup REST API")
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserService userService;

    private final AuthService authService;

    public AuthController(UserService userService, AuthServiceImpl authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @ApiOperation(value = "REST API to login user to application")
    @PostMapping("/signin")
    public ResponseEntity<JWTAuthResponse> authenticateUser(@RequestBody SignInDTO signInDto) {
        return ResponseEntity.ok(authService.createToken(signInDto));
    }

    @ApiOperation(value = "REST API to register new user to application")
    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@RequestBody SignUpDTO signUpDto) {
        userService.registerUser(signUpDto);
        return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
    }
}
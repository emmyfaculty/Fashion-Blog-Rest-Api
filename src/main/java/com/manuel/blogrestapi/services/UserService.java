package com.manuel.blogrestapi.services;

import com.manuel.blogrestapi.dto.SignUpDTO;
import com.manuel.blogrestapi.dto.UserDTO;
import com.manuel.blogrestapi.entities.User;

public interface UserService {

    UserDTO registerUser(SignUpDTO signUpDto);

    User currentLoggedUser(String token);

}
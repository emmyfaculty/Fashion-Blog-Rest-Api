package com.manuel.blogrestapi.user.unit.service;

import com.manuel.blogrestapi.entities.User;
import com.manuel.blogrestapi.models.Gender;
import com.manuel.blogrestapi.models.UserDetails;
import com.manuel.blogrestapi.repository.UserRepository;
import com.manuel.blogrestapi.entities.Role;
import com.manuel.blogrestapi.repository.RoleRepository;
import com.manuel.blogrestapi.security.JWTTokenProvider;
import com.manuel.blogrestapi.dto.SignUpDTO;
import com.manuel.blogrestapi.services.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    private static final String TEST_EMAIL = "test@test.java";

    private static final String TEST_NAME = "test_name";

    private static final Integer TEST_AGE = 99;

    private static final String TEST_CITY = "test_city";

    private static final String TEST_DESCRIPTION = "test_description";

    private static final String TEST_USERNAME = "test_user";

    private static final String TEST_ROLE = "ROLE_TEST";


    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private JWTTokenProvider jwtTokenProvider;

    @InjectMocks
    private UserServiceImpl userService;

    private SignUpDTO testSignUpDTO;

    private User testUser;

    private Role testRole;


    @BeforeEach
    public void setup() {

        testUser = User.builder()
                .email(TEST_EMAIL)
                .gender(Gender.MALE)
                .name(TEST_NAME)
                .username(TEST_USERNAME)
                .userDetails(UserDetails.builder()
                        .age(TEST_AGE)
                        .city(TEST_CITY)
                        .description(TEST_DESCRIPTION)
                        .build())
                .build();

        testRole = Role.builder()
                .name(TEST_ROLE)
                .id(3L)
                .build();

        testUser.setRoles(Collections.singleton(testRole));

        testSignUpDTO = SignUpDTO.builder()
                .name(TEST_NAME)
                .username(TEST_USERNAME)
                .email(TEST_EMAIL)
                .gender(Gender.MALE)
                .age(TEST_AGE)
                .build();
    }

    @Test
    public void givenSignUpDTOObject_whenRegisterUser_thenReturnUserDTOObject() {

        //given
        when(userRepository.existsByEmail(any(String.class))).thenReturn(false);
        when(userRepository.existsByUsername(any(String.class))).thenReturn(false);
        when(roleRepository.findByName(anyString())).thenReturn(testRole);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        //when
        var userDTO = userService.registerUser(testSignUpDTO);

        //then
        assertNotNull(userDTO.getUserDetails());
        Assertions.assertEquals(userDTO.getEmail(), TEST_EMAIL);
        Assertions.assertEquals(userDTO.getGender(), Gender.MALE);
        Assertions.assertEquals(userDTO.getName(), TEST_NAME);
        Assertions.assertEquals(userDTO.getUsername(), TEST_USERNAME);
    }

}
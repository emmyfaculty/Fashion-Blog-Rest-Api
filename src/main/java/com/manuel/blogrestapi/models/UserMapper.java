package com.manuel.blogrestapi.models;

import com.manuel.blogrestapi.dto.UserDTO;
import com.manuel.blogrestapi.dto.UserDetailsDTO;
import com.manuel.blogrestapi.entities.User;

public class UserMapper {

    public static UserDTO mapUserDTOFromUser(User user) {
        return buildUserDTO(user);
    }

    public static UserDetailsDTO mapUserDetailsDTOFromUserDetails(UserDetails userDetails) {
        return buildUserDetailsDTO(userDetails);
    }

    public static UserDTO buildUserDTO(User user){
        return UserDTO.builder()
                .name(user.getName())
                .username(user.getUsername())
                .email(user.getEmail())
                .gender(user.getGender())
                .userDetails(mapUserDetailsDTOFromUserDetails(user.getUserDetails()))
                .build();
    }

    public static UserDetailsDTO buildUserDetailsDTO(UserDetails userDetails) {
        return UserDetailsDTO.builder()
                .age(userDetails.getAge())
                .description(userDetails.getDescription())
                .city(userDetails.getCity())
                .build();
    }
}

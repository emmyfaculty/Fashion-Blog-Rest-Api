package com.manuel.blogrestapi.dto;

import com.manuel.blogrestapi.entities.Role;
import com.manuel.blogrestapi.models.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private String name;

    private UserDetailsDTO userDetails;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String username;

    private String email;

    private String password;

    private Set<Role> roles;

}
package com.manuel.blogrestapi.dto;

import com.manuel.blogrestapi.models.Gender;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@ApiModel(value = "Register model information")
@Data
@Builder
public class SignUpDTO {

    @ApiModelProperty(value = "user first name")
    private String name;

    private String username;

    @ApiModelProperty(value = "user e-mail")
    private String email;

    @ApiModelProperty(value = "user password")
    private String password;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private Integer age;

}
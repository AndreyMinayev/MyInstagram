package com.example.Instagram.payload.request;

import com.example.Instagram.annotations.PasswordMatches;
import com.example.Instagram.annotations.ValidEmail;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@PasswordMatches
public class SignupRequest {

    @Email(message = "email is invalid")
    @NotEmpty(message = "email cannot be empty")
    @ValidEmail
    private  String email;
    @NotEmpty(message = "name cannot be empty")
    private  String name;
    @NotEmpty(message = "surname cannot be empty")
    private  String surname;
    @NotEmpty(message = "username cannot be empty")
    private  String username;
    @NotEmpty(message = "password is required")
    @Size(min = 6)

    private String password;
    private String confirmPassword;


}

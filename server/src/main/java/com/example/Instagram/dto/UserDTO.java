package com.example.Instagram.dto;


import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UserDTO {
    private Long id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String surname;
    private String username;
    private String bio;


}

package com.example.dentistClinic.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractDTO {

    private Long id;
    private String name;
    private String lastName;
    private String password;
    private String username;

}

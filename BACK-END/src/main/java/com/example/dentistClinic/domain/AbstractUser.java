package com.example.dentistClinic.domain;
import lombok.ToString;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;

@ToString
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class AbstractUser implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



}

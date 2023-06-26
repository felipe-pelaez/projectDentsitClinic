package com.example.dentistClinic.emailToken;

import com.example.dentistClinic.domain.AbstractUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ConfirmationToken {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String token;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime expiresAt;

     @Column
    private LocalDateTime confirmedAt;

    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private AbstractUser user;

    public ConfirmationToken(String token, LocalDateTime createdAt, LocalDateTime expiresAt, AbstractUser user) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.user = user;
    }
}


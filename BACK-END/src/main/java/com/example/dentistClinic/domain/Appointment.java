package com.example.dentistClinic.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="apoinments")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "userPatient_id",referencedColumnName = "id")
    private UserPatient userPatient;
    @ManyToOne
    @JoinColumn(name="userDentist_id",referencedColumnName = "id")
    private UserDentist userDentist;
    @Column
    private LocalDate date;

}

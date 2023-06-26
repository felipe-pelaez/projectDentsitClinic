package com.example.dentistClinic.dto;

import com.example.dentistClinic.domain.Appointment;
import com.example.dentistClinic.domain.UserDentist;
import com.example.dentistClinic.domain.UserPatient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@NoArgsConstructor
@Data
@Component
public class AppointmentDTO {
    private Long id;
    private Long dentist_id;
    private Long user_id;
    private LocalDate date;
    private String dentist_name;
    private String user_name;

    public Appointment AppoinmentDTOToAppoinment(AppointmentDTO turnoDTO){
        Appointment apoinment= new Appointment();
        UserPatient user= new UserPatient();
        UserDentist odontologo= new UserDentist();
        apoinment.setId(turnoDTO.getId());
        apoinment.setDate(turnoDTO.getDate());
        user.setId(turnoDTO.getUser_id());
        user.setName(turnoDTO.getUser_name());
        odontologo.setId(turnoDTO.getDentist_id());
        odontologo.setName(turnoDTO.getDentist_name());
        //vincular los objetos
        apoinment.setUserDentist(odontologo);
        apoinment.setUserPatient(user);
        //el turno esta listo
        return apoinment;
    }
    public AppointmentDTO AppoinmentToAppoinmeentDTO(Appointment apoinment){
        AppointmentDTO apoinmentDTO= new AppointmentDTO();

        apoinmentDTO.setId(apoinment.getId());
        apoinmentDTO.setDentist_id(apoinment.getUserDentist().getId());
        apoinmentDTO.setUser_id(apoinment.getUserPatient().getId());
        apoinmentDTO.setDate(apoinment.getDate());
        apoinmentDTO.setDentist_name(apoinment.getUserDentist().getName());
        apoinmentDTO.setUser_name(apoinment.getUserPatient().getName());

        return apoinmentDTO;
    }

}

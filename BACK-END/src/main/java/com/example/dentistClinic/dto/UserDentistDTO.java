package com.example.dentistClinic.dto;

import com.example.dentistClinic.domain.Appointment;
import com.example.dentistClinic.domain.UserDentist;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@Component
@NoArgsConstructor
public class UserDentistDTO extends AbstractDTO {


    private String tuition;
    private Set<AppointmentDTO> appointments = new HashSet<>();
    private String username;
    private String password;
    private AppointmentDTO appointmentDTO;

    @Autowired
    public UserDentistDTO(AppointmentDTO appointmentDTO) {
        this.appointmentDTO = appointmentDTO;
    }

    public UserDentist DTOToUser(UserDentistDTO userDTO) {
        UserDentist user = new UserDentist();
        user.setId(userDTO.getId());
        user.setName(userDTO.getName());
        user.setLastName(userDTO.getLastName());
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setTuition(userDTO.getTuition());
        return user;
    }

    public UserDentistDTO userToDTO(UserDentist user) {
        UserDentistDTO userDTO = new UserDentistDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setLastName(user.getLastName());
        userDTO.setUsername(user.getUsername());
        userDTO.setPassword(user.getPassword());
        userDTO.setTuition(user.getTuition());

        Set<AppointmentDTO> appointmentDTOS = new HashSet<>();
        for (Appointment appointment:user.getApoinments()) {
            appointmentDTOS.add(appointmentDTO.AppoinmentToAppoinmeentDTO(appointment));
        }
        userDTO.setAppointments(appointmentDTOS);
        return userDTO;
    }

    public UserDentistDTO userToDTOSecure(UserDentist userDentist)
    {
        UserDentistDTO userDentistDTO = new UserDentistDTO();
        userDentistDTO.setId(userDentist.getId());
        userDentistDTO.setName(userDentist.getName());

        return userDentistDTO;
    }

}

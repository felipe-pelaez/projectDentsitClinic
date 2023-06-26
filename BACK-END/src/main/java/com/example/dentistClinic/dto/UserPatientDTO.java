package com.example.dentistClinic.dto;

import com.example.dentistClinic.domain.Address;
import com.example.dentistClinic.domain.Appointment;
import com.example.dentistClinic.domain.UserPatient;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Component
@NoArgsConstructor
public class UserPatientDTO extends AbstractDTO {


    private String document;
    private Address address;
    private Set<AppointmentDTO> apoinments;
    private String username;
    private AppointmentDTO appointmentDTO;


    @Autowired
    public UserPatientDTO(AppointmentDTO appointmentDTO) {
        this.appointmentDTO = appointmentDTO;
    }

    public UserPatient DTOToUser(UserPatientDTO userDTO) {
        UserPatient user = new UserPatient();
        user.setId(userDTO.getId());
        user.setName(userDTO.getName());
        user.setLastName(userDTO.getLastName());
        user.setUsername(userDTO.getUsername());
        user.setDocument(userDTO.getDocument());
        user.setAddress(userDTO.getAddress());
        return user;
    }

    public UserPatientDTO userToDTO(UserPatient user) {
        UserPatientDTO userDTO = new UserPatientDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setLastName(user.getLastName());
        userDTO.setUsername(user.getUsername());
        userDTO.setDocument(user.getDocument());
        userDTO.setAddress(user.getAddress());

        Set<AppointmentDTO> appointmentDTOS = new HashSet<>();
        for (Appointment appointment: user.getAppointments()) {
           appointmentDTOS.add(appointmentDTO.AppoinmentToAppoinmeentDTO(appointment));
        }
        userDTO.setApoinments(appointmentDTOS);
        return userDTO;
    }

    public UserPatientDTO userToDTOSecure(UserPatient userPatient)
    {
        UserPatientDTO userPatientDTO = new UserPatientDTO();
        userPatientDTO.setId(userPatient.getId());
        userPatientDTO.setName(userPatient.getName());
        return userPatientDTO;
    }

}

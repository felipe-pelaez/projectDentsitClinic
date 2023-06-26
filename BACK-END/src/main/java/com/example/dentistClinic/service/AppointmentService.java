package com.example.dentistClinic.service;

import com.example.dentistClinic.domain.Appointment;
import com.example.dentistClinic.domain.UserDentist;
import com.example.dentistClinic.domain.UserPatient;
import com.example.dentistClinic.dto.AppointmentDTO;
import com.example.dentistClinic.exceptions.BadRequestException;
import com.example.dentistClinic.repository.AppointmentRepository;
import com.example.dentistClinic.repository.UserDentistRepository;
import com.example.dentistClinic.repository.UserPatientRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class AppointmentService {
    private static final Logger logger = LogManager.getLogger(AppointmentService.class);
    private AppointmentRepository apoinmentRepository;
    private UserDentistRepository userDentistRepository;
    private UserPatientRepository userPatientRepository;

    private AppointmentDTO appointmentDTO;

    @Autowired
    public AppointmentService(AppointmentRepository apoinmentRepository, UserDentistRepository userDentistRepository, UserPatientRepository userPatientRepository, AppointmentDTO appointmentDTO) {
        this.apoinmentRepository = apoinmentRepository;
        this.userDentistRepository = userDentistRepository;
        this.userPatientRepository = userPatientRepository;
        this.appointmentDTO = appointmentDTO;
    }




    public AppointmentDTO saveAppoinment(AppointmentDTO appointmentDTO){
        appointmentDTO.setUser_name(userPatientRepository.findById(appointmentDTO.getUser_id()).get().getName());
        appointmentDTO.setDentist_name(userDentistRepository.findById(appointmentDTO.getDentist_id()).get().getName());
        return appointmentDTO.AppoinmentToAppoinmeentDTO(apoinmentRepository.save(appointmentDTO.AppoinmentDTOToAppoinment(appointmentDTO)));
    }

    public AppointmentDTO saveRandomAppointment(AppointmentDTO apoinmentDTO) {
        UserPatient user = userPatientRepository.findById(apoinmentDTO.getUser_id()).get();
        String nombrePaciente = user.getName();
        apoinmentDTO.setUser_name(nombrePaciente);

        List<UserDentist> dentists = userDentistRepository.findAll();
        if (dentists.isEmpty()) {
            throw new RuntimeException("No hay odontólogos registrados en la base de datos");
        }
        Random random = new Random();
        UserDentist userDentist = dentists.get(random.nextInt(dentists.size()));
        Long dentistId = userDentist.getId();
        String dentistName = userDentist.getName();
        apoinmentDTO.setDentist_id(dentistId);
        apoinmentDTO.setDentist_name(dentistName);

        return apoinmentDTO.AppoinmentToAppoinmeentDTO(
                apoinmentRepository.save(apoinmentDTO.AppoinmentDTOToAppoinment(apoinmentDTO)));
    }


    public Optional<AppointmentDTO> searchAppoinmentById(Long id){
        Optional<Appointment> searchedAppoinment= apoinmentRepository.findById(id);
        if (searchedAppoinment.isPresent()){
            logger.info("The appoinment was found");
            return Optional.of(appointmentDTO.AppoinmentToAppoinmeentDTO(searchedAppoinment.get()));
        }
        else{
            return Optional.empty();
        }
    }

    public List<AppointmentDTO> findAllAppointment()
    {
        List<Appointment> apoinments = apoinmentRepository.findAll();
        List<AppointmentDTO> apoinmentDTO = new ArrayList<>();

        for (Appointment apoinment: apoinments) {
            apoinmentDTO.add(appointmentDTO.AppoinmentToAppoinmeentDTO(apoinment));
        }

        return apoinmentDTO;
    }

    public List<AppointmentDTO> searchAppointmentsByDentistId(Long dentistId) {
        Optional<UserDentist> optionalUserDentist = userDentistRepository.findById(dentistId);

        if (optionalUserDentist.isPresent()) {
            UserDentist userDentist = optionalUserDentist.get();
            Set<Appointment> appointments = userDentist.getApoinments();

            List<AppointmentDTO> appointmentDTOs = new ArrayList<>();
            for (Appointment appointment : appointments) {
                appointmentDTOs.add(appointmentDTO.AppoinmentToAppoinmeentDTO(appointment));
            }

            return appointmentDTOs;
        } else {
            throw new IllegalArgumentException("Dentist not found with ID: " + dentistId);
        }
    }
    public void deleteAppoinment(Long id){
        apoinmentRepository.deleteById(id);
    }

    public AppointmentDTO updateAppointment(AppointmentDTO appointmentDTO)throws BadRequestException {
        if (appointmentDTO.getId() == null || appointmentDTO.getDentist_id()== null || appointmentDTO.getUser_id() == null || appointmentDTO.getDate() == null || appointmentDTO.getDentist_name() == null || appointmentDTO.getDentist_name().isEmpty() || appointmentDTO.getUser_name() == null || appointmentDTO.getUser_name().isEmpty()) {
            throw new BadRequestException("Los campos no pueden ser nulos o vacíos.");
        }
        return appointmentDTO.AppoinmentToAppoinmeentDTO(apoinmentRepository.save(appointmentDTO.AppoinmentDTOToAppoinment(appointmentDTO)));
    }


}

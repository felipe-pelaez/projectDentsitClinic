package com.example.dentistClinic.service;

import com.example.dentistClinic.domain.AbstractUser;
import com.example.dentistClinic.domain.UserAdmin;
import com.example.dentistClinic.domain.UserDentist;
import com.example.dentistClinic.domain.UserPatient;
import com.example.dentistClinic.dto.AbstractDTO;
import com.example.dentistClinic.dto.UserAdminDTO;
import com.example.dentistClinic.dto.UserDentistDTO;
import com.example.dentistClinic.dto.UserPatientDTO;
import com.example.dentistClinic.email.EmailSender;
import com.example.dentistClinic.emailToken.ConfirmationToken;
import com.example.dentistClinic.emailToken.ConfirmationTokenRespository;
import com.example.dentistClinic.emailToken.ConfirmationTokenService;
import com.example.dentistClinic.exceptions.BadRequestException;
import com.example.dentistClinic.repository.UserAdminRepository;
import com.example.dentistClinic.repository.UserDentistRepository;
import com.example.dentistClinic.repository.UserPatientRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@AllArgsConstructor
@NoArgsConstructor
@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserPatientRepository userPatientRepository;
    @Autowired
    private UserAdminRepository userAdminRepository;
    @Autowired
    private UserDentistRepository userDentistRepository;
    @Autowired
    private ConfirmationTokenRespository confirmationTokenRespository;
    @Autowired
    private RegistryService registryService;
    @Autowired
    private UserPatientDTO userPatientDTO;
    @Autowired
    private UserDentistDTO userDentistDTO;
    @Autowired
    private UserAdminDTO userAdminDTO;
    @Autowired
    private ConfirmationTokenService confirmationTokenService;
    @Autowired
    private EmailSender emailSender;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserPatient> searchedPatient = userPatientRepository.findByUsername(username);
        if (searchedPatient.isPresent()) {
            return searchedPatient.get();
        }

        Optional<UserAdmin> searchedAdmin = userAdminRepository.findByUsername(username);
        if (searchedAdmin.isPresent()) {
            return searchedAdmin.get();
        }

        Optional<UserDentist> searchedDentist = userDentistRepository.findByUsername(username);
        if (searchedDentist.isPresent()) {
            return searchedDentist.get();
        }

        throw new UsernameNotFoundException("Error. User with the email " + username + " was not found");
    }


    public Optional<AbstractDTO> findByUsername(String email) {
        Optional<UserPatient> patientOptional = userPatientRepository.findByUsername(email);
        if (patientOptional.isPresent()) {
            UserPatientDTO userDTO = userPatientDTO.userToDTO(patientOptional.get());
            return Optional.of(userDTO);
        }

        Optional<UserAdmin> adminOptional = userAdminRepository.findByUsername(email);
        if (adminOptional.isPresent()) {
            UserAdminDTO userDTO = userAdminDTO.userToDTO(adminOptional.get());
            return Optional.of(userDTO);
        }

        Optional<UserDentist> dentistOptional = userDentistRepository.findByUsername(email);
        if (dentistOptional.isPresent()) {
            UserDentistDTO userDTO = userDentistDTO.userToDTO(dentistOptional.get());
            return Optional.of(userDTO);
        }

        return Optional.empty();
    }



    public Optional<AbstractDTO> findById(Long id) {
        Optional<UserPatient> patientOptional = userPatientRepository.findById(id);
        if (patientOptional.isPresent()) {
            UserPatientDTO userDTO = userPatientDTO.userToDTO(patientOptional.get());
            return Optional.of(userDTO);
        }

        Optional<UserAdmin> adminOptional = userAdminRepository.findById(id);
        if (adminOptional.isPresent()) {
            UserAdminDTO userDTO = userAdminDTO.userToDTO(adminOptional.get());
            return Optional.of(userDTO);
        }

        Optional<UserDentist> dentistOptional = userDentistRepository.findById(id);
        if (dentistOptional.isPresent()) {
            UserDentistDTO userDTO = userDentistDTO.userToDTO(dentistOptional.get());
            return Optional.of(userDTO);
        }

        return Optional.empty();
    }



    @Transactional
    public String updateUser(AbstractUser abstractUser) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String token = UUID.randomUUID().toString();


        if (abstractUser instanceof UserPatient) {
            UserPatient userToUpdate = (UserPatient) abstractUser;
            ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), userToUpdate);
            if(abstractUser.getPassword() != null && !abstractUser.getPassword().isEmpty())
            {
                String encodedPassword = passwordEncoder.encode(abstractUser.getPassword());
                userToUpdate.setPassword(encodedPassword);
            }
            else
            {
            userToUpdate.setPassword(userPatientRepository.findById(userToUpdate.getId()).get().getPassword());
            }
            if(userPatientRepository.findById(userToUpdate.getId()).get().getUsername().equals(userToUpdate.getUsername()))
            {
            userToUpdate.setEnabled(true);
            }
            else
            {
                confirmationTokenService.saveConfirmationToken(confirmationToken);
                String link = "http://localhost:8080/confirmToken/" + token;
                emailSender.send(userToUpdate.getUsername(), registryService.buildEmail(userToUpdate.getName(), link));
            }
            userPatientRepository.save(userToUpdate);

            return userToUpdate.getUsername();
        } else if (abstractUser instanceof UserAdmin) {
            UserAdmin userToUpdate = (UserAdmin) abstractUser;
            ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), userToUpdate);
            if(abstractUser.getPassword() != null && !abstractUser.getPassword().isEmpty())
            {
                String encodedPassword = passwordEncoder.encode(abstractUser.getPassword());
                userToUpdate.setPassword(encodedPassword);
            }
            else
            {
                userToUpdate.setPassword(userAdminRepository.findByUsername(abstractUser.getUsername()).get().getPassword());
            }
            if (userAdminRepository.findById(userToUpdate.getId()).get().getUsername().equals(userToUpdate.getUsername())) {
                userToUpdate.setEnabled(true);
            } else {
                System.out.println("The email changed: " + userToUpdate.getUsername() + " " + userAdminRepository.findById(userToUpdate.getId()).get().getUsername());
                confirmationTokenService.saveConfirmationToken(confirmationToken);
                String link = "http://localhost:8080/confirmToken/" + token;
                emailSender.send(userToUpdate.getUsername(), registryService.buildEmail(userToUpdate.getName(), link));
            }

            userAdminRepository.save(userToUpdate);

            return userToUpdate.getUsername();

        } else if (abstractUser instanceof UserDentist) {
            UserDentist userToUpdate = (UserDentist) abstractUser;
            ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), userToUpdate);
            if(abstractUser.getPassword() != null && !abstractUser.getPassword().isEmpty())
            {
                String encodedPassword = passwordEncoder.encode(abstractUser.getPassword());
                userToUpdate.setPassword(encodedPassword);
            }
            else
            {
                userToUpdate.setPassword(userDentistRepository.findByUsername(abstractUser.getUsername()).get().getPassword());
            }
            if(userDentistRepository.findById(userToUpdate.getId()).get().getUsername().equals(userToUpdate.getUsername()))
            {
                userToUpdate.setEnabled(true);
            }
            else
            {
                confirmationTokenService.saveConfirmationToken(confirmationToken);
                String link = "http://localhost:8080/confirmToken/" + token;
                emailSender.send(userToUpdate.getUsername(), registryService.buildEmail(userToUpdate.getName(), link));
            }
            userDentistRepository.save(userToUpdate);

            return userToUpdate.getUsername();
        } else {
            throw new RuntimeException("The user with this username was not found: " + abstractUser.getUsername());
        }
    }

    public List<UserPatientDTO> findAllPatients() {
        List<UserPatient> users = userPatientRepository.findAll();
        List<UserPatientDTO> userDTOs = new ArrayList<>();

        for (UserPatient user : users) {
            UserPatientDTO userDTO = userPatientDTO.userToDTO(user);
            userDTOs.add(userDTO);
        }

        return userDTOs;
    }

    public List<UserDentistDTO> findAllDentists() {
        List<UserDentist> users = userDentistRepository.findAll();
        List<UserDentistDTO> userDTOs = new ArrayList<>();

        for (UserDentist user : users) {
            UserDentistDTO userDTO = userDentistDTO.userToDTO(user);
            userDTOs.add(userDTO);
        }

        return userDTOs;
    }

    public String createUser(AbstractUser user) throws BadRequestException {
        BCryptPasswordEncoder cifrador = new BCryptPasswordEncoder();
        String passwordEncriptada = cifrador.encode(user.getPassword());

        if (user instanceof UserPatient) {
            ((UserPatient) user).setPassword(passwordEncriptada);
            registryService.saveUser(user);
        } else if (user instanceof UserAdmin) {
            ((UserAdmin) user).setPassword(passwordEncriptada);
            registryService.saveUser(user);
        } else if (user instanceof UserDentist) {
            ((UserDentist) user).setPassword(passwordEncriptada);
            registryService.saveUser(user);
        } else {
            throw new BadRequestException("Invalid user type");
        }

        return "The user was created.";
    }




    public void deleteUserById(Long id) {
        Optional<UserPatient> patientOptional = userPatientRepository.findById(id);
        if (patientOptional.isPresent()) {
            confirmationTokenRespository.deleteByUser(patientOptional.get());
            userPatientRepository.deleteById(id);
            return;
        }

        Optional<UserAdmin> adminOptional = userAdminRepository.findById(id);
        if (adminOptional.isPresent()) {
            confirmationTokenRespository.deleteByUser(adminOptional.get());
            userAdminRepository.deleteById(id);
            return;
        }

        Optional<UserDentist> dentistOptional = userDentistRepository.findById(id);
        if (dentistOptional.isPresent()) {
            confirmationTokenRespository.deleteByUser(dentistOptional.get());
            userDentistRepository.deleteById(id);
            return;
        }

        throw new RuntimeException("Error. User with ID " + id + " was not found.");
    }


    public List<UserDentistDTO> findDentistsNames() {
        List<UserDentist> dentists = userDentistRepository.findAll();
        List<UserDentistDTO> dentistDTOs = new ArrayList<>();

        for (UserDentist dentist : dentists) {
            UserDentistDTO dto = userDentistDTO.userToDTOSecure(dentist);
            dentistDTOs.add(dto);
        }

        return dentistDTOs;
    }

    public List<UserPatientDTO> findPatientsNames()
    {
        List<UserPatient> patients = userPatientRepository.findAll();
        List<UserPatientDTO> patientDTOS = new ArrayList<>();
        for (UserPatient userPatient: patients) {
            patientDTOS.add(userPatientDTO.userToDTOSecure(userPatient));
        }
        return patientDTOS;
    }






}

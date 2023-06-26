package com.example.dentistClinic.controller;
import com.example.dentistClinic.dto.AbstractDTO;
import com.example.dentistClinic.dto.AppointmentDTO;
import com.example.dentistClinic.dto.UserDentistDTO;
import com.example.dentistClinic.dto.UserPatientDTO;
import com.example.dentistClinic.exceptions.BadRequestException;
import com.example.dentistClinic.exceptions.ResourceNotFoundException;
import com.example.dentistClinic.service.AppointmentService;
import com.example.dentistClinic.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@CrossOrigin(origins = "http://127.0.0.1:5173", allowCredentials = "true")
@RestController
@RequestMapping("/appointments")
public class AppointmentCotroller {
    private static final Logger logger = LogManager.getLogger(AppointmentCotroller.class);
    private AppointmentService apoinmentService;
    private UserService userService;

    @Autowired
    public AppointmentCotroller(AppointmentService apoinmentService, UserService userService) {
        this.apoinmentService = apoinmentService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<AppointmentDTO> registerAppointment(@RequestBody AppointmentDTO apoinmentDTO) throws ResourceNotFoundException {
        ResponseEntity<AppointmentDTO> resp;
        Optional<AbstractDTO> searchedPatient= userService.findById(apoinmentDTO.getUser_id());
        Optional<AbstractDTO> searchedDentist= userService.findById(apoinmentDTO.getDentist_id());
        if (searchedPatient.isPresent() && searchedDentist.isPresent()){
            resp=ResponseEntity.ok(apoinmentService.saveAppoinment(apoinmentDTO));
            logger.info("the appointment was registered");
            return resp;
        }
        else{
            throw new ResourceNotFoundException("Dentist or Patient were not found");
        }

    }

        @PostMapping("/random")
        public ResponseEntity<String> saveRandomApoinment(@RequestBody AppointmentDTO appointmentDTO) throws ResourceNotFoundException{
            AppointmentDTO appoinmentToSave = apoinmentService.saveRandomAppointment(appointmentDTO);
            return ResponseEntity.ok(appoinmentToSave.getDentist_name() + userService.findById(appoinmentToSave.getId()).get().getLastName());
        }

    @GetMapping("/byDentist/{id}")
    public ResponseEntity<List<AppointmentDTO>> SearchApoinmentByDentist(@PathVariable Long id){
        List<AppointmentDTO> searchedAppointment = apoinmentService.searchAppointmentsByDentistId(id);
            return ResponseEntity.ok(searchedAppointment);
    }

    @GetMapping("/findDentistName")
    public ResponseEntity<List<UserDentistDTO>> findAllDentists(){
        List<UserDentistDTO> searchedDetists = userService.findDentistsNames();
            return ResponseEntity.ok(searchedDetists);
    }

    @GetMapping("/findPatientsNames")
    public ResponseEntity<List<UserPatientDTO>> findPatientsNames()
    {
        return ResponseEntity.ok(userService.findPatientsNames());
    }

    @GetMapping
    public ResponseEntity<List<AppointmentDTO>> findAll()
    {
        return ResponseEntity.ok(apoinmentService.findAllAppointment());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAppoinment(@PathVariable Long id) throws ResourceNotFoundException{
        Optional<AppointmentDTO> searchedAppoinment= apoinmentService.searchAppoinmentById(id);
        if (searchedAppoinment.isPresent()){
            apoinmentService.deleteAppoinment(id);

            return ResponseEntity.ok("the appoinment with this id was deleted = " +id);
        }
        else{
            throw new ResourceNotFoundException("Error. The appoinment with this id was not found= "+id);
        }
    }

    @PutMapping
    public ResponseEntity<AppointmentDTO> updateAppoinment(@RequestBody AppointmentDTO appointmentDTO)throws BadRequestException {
        return  ResponseEntity.ok(apoinmentService.updateAppointment(appointmentDTO));
    }
}

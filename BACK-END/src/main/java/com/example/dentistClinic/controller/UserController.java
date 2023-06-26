package com.example.dentistClinic.controller;

import com.example.dentistClinic.domain.AbstractUser;
import com.example.dentistClinic.domain.UserAdmin;
import com.example.dentistClinic.domain.UserDentist;
import com.example.dentistClinic.domain.UserPatient;
import com.example.dentistClinic.dto.AbstractDTO;
import com.example.dentistClinic.dto.UserDentistDTO;
import com.example.dentistClinic.dto.UserPatientDTO;
import com.example.dentistClinic.exceptions.BadRequestException;
import com.example.dentistClinic.jwt.JwtUtil;
import com.example.dentistClinic.service.RegistryService;
import com.example.dentistClinic.service.UserService;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@CrossOrigin(origins = "http://127.0.0.1:5173", allowCredentials = "true")
@RestController
@RequestMapping("/users")
public class UserController {


    private UserService userService;

    private RegistryService registryService;

    private DaoAuthenticationProvider daoAuthenticationProvider;

    private JwtUtil jwtUtil;


    public UserController(UserService userService, RegistryService registryService, DaoAuthenticationProvider daoAuthenticationProvider, JwtUtil jwtUtil) {
        this.userService = userService;
        this.registryService = registryService;
        this.daoAuthenticationProvider = daoAuthenticationProvider;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> data) {
        String username = data.get("username");
        String password = data.get("password");

        Authentication authentication = daoAuthenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = userService.loadUserByUsername(username);
        String token = jwtUtil.generateToken(userDetails);
        JSONObject response = new JSONObject();
        response.put("token", token);
        response.put("name", userService.findByUsername(username).get().getName());
        response.put("userId", userService.findByUsername(username).get().getId());

        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        if (authorities.contains(new SimpleGrantedAuthority("PATIENT"))) {
            response.put("userType", "PATIENT");
        } else if (authorities.contains(new SimpleGrantedAuthority("ADMIN"))) {
            response.put("userType", "ADMIN");
        } else if (authorities.contains(new SimpleGrantedAuthority("DENTIST"))) {
            response.put("userType", "DENTIST");
        }

        return ResponseEntity.ok(response.toString());
    }

    @PostMapping("/create-user")
    public ResponseEntity<String> createUser(@RequestBody UserPatient user) throws BadRequestException {
        System.out.println(user);
        if (userService.findByUsername(user.getUsername()) != null && !userService.findByUsername(user.getUsername()).isEmpty()) {
            return ResponseEntity.badRequest().body("The user already exists.");
        }

        return ResponseEntity.ok(userService.createUser(user));
    }

    @PostMapping("/create-admin")
    public ResponseEntity<String> createAdmin(@RequestBody UserAdmin user) throws BadRequestException {

        if (userService.findByUsername(user.getUsername()) != null && !userService.findByUsername(user.getUsername()).isEmpty()) {
            return ResponseEntity.badRequest().body("The user already exists.");
        }
        return ResponseEntity.ok(userService.createUser(user));

    }

    @PostMapping("/create-dentist")
    public ResponseEntity<String> createDentist(@RequestBody UserDentist user) throws BadRequestException {

        if (userService.findByUsername(user.getUsername()) != null && !userService.findByUsername(user.getUsername()).isEmpty()) {
            return ResponseEntity.badRequest().body("The user already exists.");
        }
        return ResponseEntity.ok(userService.createUser(user));

    }

    @GetMapping("/confirmToken/{token}")
    public String confirmToken(@PathVariable String token)
    {
        return registryService.confirmToken(token);
    }

    @PutMapping
    public String updateUser(@RequestBody UserPatient user)
    {
        return userService.updateUser(user);
    }

    @PutMapping("/updateDentist")
    public String updateDentist(@RequestBody UserDentist user)
    {
        System.out.println(user);
        return userService.updateUser(user);
    }

    @PutMapping("/updateAdmin")
    public String updateDentist(@RequestBody UserAdmin user)
    {
        return userService.updateUser(user);
    }


    @GetMapping("/getPatients")
    public ResponseEntity<List<UserPatientDTO>>findAllPatients()
    {
        return ResponseEntity.ok(userService.findAllPatients());
    }

    @GetMapping("/getDentists")
    public ResponseEntity<List<UserDentistDTO>>findAllDentists()
    {
        return ResponseEntity.ok(userService.findAllDentists());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long id)
    {
        userService.deleteUserById(id);
        return ResponseEntity.ok("The user was deleted");
    }


    @GetMapping("/{id}")
    public ResponseEntity<AbstractDTO> findById(@PathVariable Long id) {
        Optional<AbstractDTO> userDTO = userService.findById(id);
        if (userDTO.isPresent()) {
            return ResponseEntity.ok(userDTO.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }



}

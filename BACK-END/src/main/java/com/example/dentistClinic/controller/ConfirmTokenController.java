package com.example.dentistClinic.controller;

import com.example.dentistClinic.service.RegistryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://127.0.0.1:5173", allowCredentials = "true")
@RestController
@RequestMapping("/confirmToken")
public class ConfirmTokenController {

    private RegistryService registryService;

    @Autowired
    public ConfirmTokenController(RegistryService registryService) {
        this.registryService = registryService;
    }

    @GetMapping("{token}")
    public String confirmToken(@PathVariable String token)
    {
        return registryService.confirmToken(token);
    }
}

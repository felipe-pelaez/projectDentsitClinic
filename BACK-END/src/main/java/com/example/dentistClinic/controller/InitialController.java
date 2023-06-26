package com.example.dentistClinic.controller;

import com.example.dentistClinic.domain.Message;
import com.example.dentistClinic.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001", "http://127.0.0.1:5173", "http://127.0.0.1:5174" }, allowCredentials = "true")
@RestController
@RequestMapping("/initial")
public class InitialController {

    @Autowired
    private MessageService messageService;

    @GetMapping("/{user1}/{user2}")
    public List<Message> handleInitialRequest(@PathVariable("user1") String user1, @PathVariable("user2") String user2) {
        List<Message> messages = messageService.chatMessages(user1, user2);
        return messages;
    }

}


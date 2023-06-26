package com.example.dentistClinic.controller;

import com.example.dentistClinic.domain.Message;
import com.example.dentistClinic.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001", "http://127.0.0.1:5173/"}, allowCredentials = "true")
@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private MessageService messageService;

    @MessageMapping("/message")
    public void recMessage(@Payload Message message) {
        if(message.getReceiverName() != "" && message.getReceiverName() != null)
        {
        simpMessagingTemplate.convertAndSendToUser(message.getReceiverName(),"/private",message);
        messageService.saveMessage(message);
        System.out.println(messageService.findAllMessages());
        }
}
}


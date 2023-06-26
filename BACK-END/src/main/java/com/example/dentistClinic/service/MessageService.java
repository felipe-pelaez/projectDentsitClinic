package com.example.dentistClinic.service;

import com.example.dentistClinic.domain.Message;
import com.example.dentistClinic.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    MessageRepository messageRepository;

    public Message saveMessage(Message message)
    {
        return messageRepository.save(message);
    }

    public List<Message> findAllMessages()
    {
        return messageRepository.findAll();
    }

    public List<Message> chatMessages(String user1, String user2) {
        List<Message> mensajes = messageRepository.findMessagesBetweenUsers(user1, user2);
        return mensajes;
    }



}

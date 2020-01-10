package com.phyohtet.chat.controller;

import com.phyohtet.chat.model.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Map;

@Controller
public class ChatController {

    @Autowired
    private SimpMessageSendingOperations operations;

    @Qualifier("session-store")
    @Autowired
    private Map<String, String> sessionStore;

    @MessageMapping("/chat.sendMessage")
    @SendTo("/msg/public")
    public ChatMessage sendMessage(@Payload ChatMessage message) {
        return message;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/msg/public")
    public ChatMessage addUser(@Payload ChatMessage message, SimpMessageHeaderAccessor headerAccessor) {
        System.out.println("Session: " + headerAccessor.getSessionId());
        sessionStore.put(message.getSender(), headerAccessor.getSessionId());
        headerAccessor.getSessionAttributes().put("username", message.getSender());
        return message;
    }

    @MessageMapping("/chat.securedMessage")
    public void sendSpecific(@Payload ChatMessage msg, Principal user, @Header("simpSessionId") String sessionId) {
        ChatMessage out = new ChatMessage();
        out.setMessageType(msg.getMessageType());
        out.setContent(msg.getContent());
        out.setSender(msg.getSender());
        out.setTo(msg.getTo());

        String to = sessionStore.get(msg.getTo());
        System.out.println("specific: " + to);

        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor
                .create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(to);
        headerAccessor.setLeaveMutable(true);

        operations.convertAndSendToUser(to, "/queue/specific-user", out, headerAccessor.getMessageHeaders());
    }

}

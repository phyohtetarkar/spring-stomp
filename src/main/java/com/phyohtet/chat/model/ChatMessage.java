package com.phyohtet.chat.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ChatMessage {

    public enum MessageType {
        CHAT, JOIN, LEAVE
    }

    @JsonProperty("message_type")
    private MessageType messageType;
    private String content;
    private String sender;
    private String to;

}

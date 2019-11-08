package com.simbirsoft.chat.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MessageDTO {
    @JsonProperty("content")
    private final String content;

    @JsonProperty("sender")
    private final String sender;

    public MessageDTO(String content, String sender) {
        this.content = content;
        this.sender = sender;
    }
}

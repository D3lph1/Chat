package com.simbirsoft.chat.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChatHistoryDTO {
    @JsonProperty("content")
    private final String content;

    @JsonProperty("fromCurrent")
    private final boolean fromCurrent;

    public ChatHistoryDTO(String content, boolean fromCurrent) {
        this.content = content;
        this.fromCurrent = fromCurrent;
    }
}

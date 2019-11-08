package com.simbirsoft.bot.http;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * It decodes JSON string into hierarchical program object.
 */
public class ApiResponseDecoder {
    private final ObjectMapper mapper;

    public ApiResponseDecoder(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public JsonNode decode(String response) throws IOException {
        return mapper.readTree(response);
    }
}

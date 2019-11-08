package com.simbirsoft.bot.http;

import com.fasterxml.jackson.databind.JsonNode;
import com.simbirsoft.bot.message.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class that extracts useful information from JSON object and returns it.
 */
public class ResponseToObjectConverter {
    /**
     * Private constructor to ensure non-instantiation because this class
     * contains only static methods.
     */
    private ResponseToObjectConverter() {
    }

    public static String toStatus(JsonNode response) {
        return response.get("status").asText();
    }

    public static String toToken(JsonNode response) {
        return response.get("data").get("token").asText();
    }

    public static List<Message> messages(JsonNode response) {
        List<Message> messages = new ArrayList<>();
        for (JsonNode messageNode : response.get("data").get("messages")) {
            Message message = new Message(
                    messageNode.get("content").asText(),
                    messageNode.get("sender").asText()
            );

            messages.add(message);
        }

        return messages;
    }
}

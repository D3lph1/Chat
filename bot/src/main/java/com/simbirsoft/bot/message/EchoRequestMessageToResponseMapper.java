package com.simbirsoft.bot.message;

/**
 * This implementation simply returns a message that was accepted.
 */
public class EchoRequestMessageToResponseMapper implements RequestMessageToResponseMapper {
    @Override
    public String map(Message message) {
        return message.getContent();
    }
}

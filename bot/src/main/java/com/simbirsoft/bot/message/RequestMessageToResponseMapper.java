package com.simbirsoft.bot.message;

/**
 * Define interface that transforms received message and returns it back as a response.
 */
public interface RequestMessageToResponseMapper {
    String map(Message message) throws Exception;
}

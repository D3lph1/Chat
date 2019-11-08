package com.simbirsoft.bot;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simbirsoft.bot.http.*;
import com.simbirsoft.bot.message.Message;
import com.simbirsoft.bot.message.RequestMessageToResponseMapper;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;

public class Bot {
    private static final String STATUS_SUCCESS = "success";

    private final Logger logger = Logger.getLogger("Bot");

    private final String email;

    private final String password;

    private final UrlResolver urlResolver;

    private final ApiRequestPerformer requestPerformer;

    private final ApiResponseDecoder responseDecoder;

    private String token;

    public Bot(URL url, String email, String password) throws Exception {
        this.email = email;
        this.password = password;
        urlResolver = new UrlResolver(url);
        requestPerformer = new UrlApiRequestPerformer();
        responseDecoder = new ApiResponseDecoder(new ObjectMapper());
        login();
    }

    private void login() throws Exception {
        logger.info(String.format("Attempting to login with email \"%s\"...", email));
        JsonNode response = performAndDecode(urlResolver.login(email, password));

        if (!ResponseToObjectConverter.toStatus(response).equals(STATUS_SUCCESS)) {
            throw new Exception("Can not login. Unexpected status of response: " + response.toString());
        }

        token = ResponseToObjectConverter.toToken(response);
        logger.info("Logged in.");
    }

    public void run(RequestMessageToResponseMapper responseMapper) throws Exception {
        while (true) {
            JsonNode response = performAndDecode(urlResolver.unreadMessages(token));
            if (!ResponseToObjectConverter.toStatus(response).equals(STATUS_SUCCESS)) {
                throw new Exception("Can not get messages");
            }

            Collection<Message> messages = ResponseToObjectConverter.messages(response);
            for (Message message : messages) {
                logger.info(String.format("Given message \"%s\" from %s.", message.getContent(), message.getSender()));

                response = performAndDecode(
                        urlResolver.send(message.getSender(), responseMapper.map(message), token)
                );
                if (ResponseToObjectConverter.toStatus(response).equals(STATUS_SUCCESS)) {
                    logger.info(String.format("Message \"%s\" sent to %s.", message.getContent(), message.getSender()));
                } else {
                    logger.error("Can not send message. Unexpected status of response: " + response.toString());
                }
            }

            Thread.sleep(100);
        }
    }

    private JsonNode performAndDecode(URL url) throws IOException {
        return responseDecoder.decode(requestPerformer.perform(url));
    }
}

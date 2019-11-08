package com.simbirsoft.chat.controller;

import com.simbirsoft.chat.dto.MessageDTO;
import com.simbirsoft.chat.exceptions.AccessDeniedException;
import com.simbirsoft.chat.exceptions.UserNotFoundException;
import com.simbirsoft.chat.service.api.ApiMessageService;
import com.simbirsoft.chat.service.auth.UserTokenAuthenticationService;
import com.simbirsoft.chat.service.http.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * This controller handles all API requests including API login.
 */
@RestController
@RequestMapping(value = "/api")
public class ApiController {
    private final UserTokenAuthenticationService authenticationService;

    private final ApiMessageService apiMessageService;

    private final SimpMessageSendingOperations simpMessageSendingOperations;

    public ApiController(
            UserTokenAuthenticationService authenticationService,
            ApiMessageService apiMessageService,
            SimpMessageSendingOperations simpMessageSendingOperations) {
        this.authenticationService = authenticationService;
        this.apiMessageService = apiMessageService;
        this.simpMessageSendingOperations = simpMessageSendingOperations;
    }

    /**
     * Method accepts bot email and password and returns authentication token which can be
     * used to get access to another API functionality.
     * Given token further used by {@link UserTokenAuthenticationService}.
     *
     * @param email Bot email
     * @param password Bot password
     * @return Response object with data.token key.
     *
     * @see UserTokenAuthenticationService
     * @see com.simbirsoft.chat.service.auth.TokenAuthenticationFilter
     * @see com.simbirsoft.chat.service.auth.TokenAuthenticationProvider
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ResponseEntity<Response> login(String email, String password) {
        try {
            String token = authenticationService.login(email, password);
            return new ResponseEntity<>(new Response(Response.STATUS_SUCCESS).add("token", token), HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(new Response("invalid_credentials"), HttpStatus.NOT_FOUND);
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>(new Response("access_only_for_bots"), HttpStatus.FORBIDDEN);
        }
    }

    /**
     * Returns collection of not read yet messages for an authenticated user.
     */
    @RequestMapping(value = "/messages/unread", method = RequestMethod.GET)
    public ResponseEntity<Response> messages(Authentication auth) {
        return messagesResult(apiMessageService.readAndMark(auth));
    }

    private ResponseEntity<Response> messagesResult(Collection<MessageDTO> DTOs) {
        return new ResponseEntity<>(
                new Response(Response.STATUS_SUCCESS).add("messages", DTOs),
                HttpStatus.OK
        );
    }

    /**
     * Send message to the specified user.
     * @param receiver User which will accept message
     * @param message Message content
     * @param auth Current authentication session
     * @return
     */
    @RequestMapping(value = "/send", method = RequestMethod.GET)
    public ResponseEntity<Response> send(@RequestParam String receiver,
                                         @RequestParam String message,
                                         Authentication auth) {
        try {
            apiMessageService.send(receiver, message, auth);

            ResponseEntity<Response> response = new ResponseEntity<>(
                    new Response("success")
                            .add("sender", auth.getName())
                            .add("content", message),
                    HttpStatus.OK
            );

            simpMessageSendingOperations.convertAndSendToUser(receiver, "/queue/send", response);
            // It is also need to send message to myself to ensure correct application work
            // in case when user logged in twice (or more) at different browsers, devices
            // and so on.
            simpMessageSendingOperations.convertAndSendToUser(auth.getName(), "/queue/send", response);

            return new ResponseEntity<>(
                    new Response(Response.STATUS_SUCCESS),
                    HttpStatus.OK
            );
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(
                    new Response("user_not_found"),
                    HttpStatus.NOT_FOUND
            );
        }
    }
}

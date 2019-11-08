package com.simbirsoft.chat.controller;

import com.simbirsoft.chat.dto.ChatHistoryDTO;
import com.simbirsoft.chat.dto.OnlineUserDTO;
import com.simbirsoft.chat.exceptions.UserNotFoundException;
import com.simbirsoft.chat.exceptions.UserOfflineException;
import com.simbirsoft.chat.service.chat.ChatHistoryService;
import com.simbirsoft.chat.service.chat.ChatSendMessageService;
import com.simbirsoft.chat.service.http.Response;
import com.simbirsoft.chat.service.ws.OnlineUsersService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This controller handles all user interacts with chat.
 */
@Controller
public class ChatController {
    private final OnlineUsersService onlineUsersService;

    private final ChatHistoryService chatHistoryService;

    private final ChatSendMessageService chatSendMessageService;

    private final SimpMessageSendingOperations simpMessageSendingOperations;

    public ChatController(
            OnlineUsersService onlineUsersService,
            ChatHistoryService chatHistoryService,
            ChatSendMessageService chatSendMessageService,
            SimpMessageSendingOperations simpMessageSendingOperations) {
        this.onlineUsersService = onlineUsersService;
        this.chatHistoryService = chatHistoryService;
        this.chatSendMessageService = chatSendMessageService;
        this.simpMessageSendingOperations = simpMessageSendingOperations;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET, name = "chat.render")
    public ModelAndView render(Authentication auth) throws UserNotFoundException {
        List<OnlineUserDTO> users = onlineUsersService.online(auth);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("users", users);
        parameters.put("me", onlineUsersService.me(auth));

        return new ModelAndView("chat", parameters);
    }

    /**
     * Returns chat conversation between given and authenticated user.
     * @param receiverEmail
     * @param auth Current authentication session
     * @return
     * @throws UserNotFoundException
     */
    @RequestMapping(value = "/chat", method = RequestMethod.GET, name = "chat.history")
    public ResponseEntity<Response> chatHistory(@RequestParam String receiverEmail, Authentication auth)
            throws UserNotFoundException {
        List<ChatHistoryDTO> history = chatHistoryService.retrieveForUser(receiverEmail, auth);

        return new ResponseEntity<>(
                new Response("success").add("history", history),
                HttpStatus.OK
        );
    }

    /**
     * Sends message to the specified user
     * @param receiverEmail User which will accept message
     * @param content Message content
     * @param auth Current authentication session
     * @return
     * @throws UserNotFoundException
     */
    @RequestMapping(value = "/send", method = RequestMethod.POST, name = "chat.send")
    public ResponseEntity<Response> send(
            @RequestParam String receiverEmail,
            @RequestParam String content,
            Authentication auth) throws UserNotFoundException {

        try {
            chatSendMessageService.send(receiverEmail, content, auth);

            ResponseEntity<Response> responseEntity = new ResponseEntity<>(
                    new Response(Response.STATUS_SUCCESS)
                            .add("sender", auth.getName())
                            .add("content", content),
                    HttpStatus.OK
            );

            simpMessageSendingOperations.convertAndSendToUser(receiverEmail, "/queue/send", responseEntity);
            // It is also need to send message to myself to ensure correct application work
            // in case when user logged in twice (or more) at different browsers, devices
            // and so on.
            simpMessageSendingOperations.convertAndSendToUser(auth.getName(), "/queue/send", responseEntity);

            return new ResponseEntity<>(new Response(Response.STATUS_SUCCESS), HttpStatus.OK);
        } catch (UserOfflineException e) {
            return new ResponseEntity<>(new Response("user_offline"), HttpStatus.CONFLICT);
        }
    }
}

package com.simbirsoft.chat.service.ws;

import com.simbirsoft.chat.dto.OnlineUserDTO;
import com.simbirsoft.chat.exceptions.UserNotFoundException;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketEventListener {
    private SimpMessageSendingOperations messagingTemplate;

    private final OnlineUsersService onlineUsersService;

    public WebSocketEventListener(
            SimpMessageSendingOperations messagingTemplate,
            OnlineUsersService onlineUsersService) {
        this.messagingTemplate = messagingTemplate;
        this.onlineUsersService = onlineUsersService;
    }

    @EventListener
    public void onConnect(SessionConnectedEvent event) throws UserNotFoundException {
        OnlineUserDTO user = onlineUsersService.me(event.getUser().getName());

        messagingTemplate.convertAndSend("/broker/connected", user);
    }

    @EventListener
    public void onDisconnect(SessionDisconnectEvent event) throws UserNotFoundException {
        OnlineUserDTO user = onlineUsersService.me(event.getUser().getName());

        messagingTemplate.convertAndSend("/broker/disconnected", user);
    }
}

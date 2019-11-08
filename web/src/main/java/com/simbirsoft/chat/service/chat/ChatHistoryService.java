package com.simbirsoft.chat.service.chat;

import com.simbirsoft.chat.dto.ChatHistoryDTO;
import com.simbirsoft.chat.entity.Message;
import com.simbirsoft.chat.entity.User;
import com.simbirsoft.chat.exceptions.UserNotFoundException;
import com.simbirsoft.chat.repository.MessageRepository;
import com.simbirsoft.chat.repository.UserRepository;
import com.simbirsoft.chat.util.UserUtil;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class ChatHistoryService extends AbstractChatService {
    private final MessageRepository messageRepository;

    public ChatHistoryService(UserRepository userRepository, MessageRepository messageRepository) {
        super(userRepository);
        this.messageRepository = messageRepository;
    }

    public List<ChatHistoryDTO> retrieveForUser(String receiverEmail, Authentication auth) throws UserNotFoundException {
        User sender = UserUtil.retrieveUserFromAuth(auth);
        User receiver = retrieveUserByEmail(receiverEmail);

        List<ChatHistoryDTO> history = new ArrayList<>();
        Collection<Message> messages = messageRepository.findBySenderAndReceiverAndViceVersaOrderByCreatedAtASC(sender, receiver);
        messages.forEach(message -> history.add(
                new ChatHistoryDTO(message.getContent(), message.getSender().equals(sender)))
        );

        return history;
    }
}

package com.simbirsoft.chat.service.api;

import com.simbirsoft.chat.dto.MessageDTO;
import com.simbirsoft.chat.entity.Message;
import com.simbirsoft.chat.entity.User;
import com.simbirsoft.chat.exceptions.UserNotFoundException;
import com.simbirsoft.chat.repository.MessageRepository;
import com.simbirsoft.chat.repository.UserRepository;
import com.simbirsoft.chat.util.UserUtil;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ApiMessageService {
    private final MessageRepository messageRepository;

    private final UserRepository userRepository;

    private final SimpMessageSendingOperations simpMessageSendingOperations;

    public ApiMessageService(
            MessageRepository messageRepository,
            UserRepository userRepository,
            SimpMessageSendingOperations simpMessageSendingOperations) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.simpMessageSendingOperations = simpMessageSendingOperations;
    }

    public Collection<MessageDTO> readAndMark(Authentication auth) {
        User user = UserUtil.retrieveUserFromAuth(auth);
        Collection<Message> unread = messageRepository.findUnreadWithSenderByReceiver(user);
        if (unread.size() != 0) {
            messageRepository.markAsReadByReceiver(user);
        }
        List<MessageDTO> DTOs = new ArrayList<>();
        unread.forEach(each -> DTOs.add(new MessageDTO(
                each.getContent(),
                each.getSender().getEmail()))
        );

        return DTOs;
    }

    public void send(String receiverEmail, String messageContent, Authentication auth) throws UserNotFoundException {
        User sender = UserUtil.retrieveUserFromAuth(auth);
        Optional<User> mbReceiver = userRepository.findByEmail(receiverEmail);
        User receiver = mbReceiver.orElseThrow(() -> UserNotFoundException.byEmail(receiverEmail));
        Message message = new Message(messageContent, sender, receiver);
        messageRepository.save(message);
    }
}

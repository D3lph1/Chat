package com.simbirsoft.chat.service.chat;

import com.simbirsoft.chat.entity.Message;
import com.simbirsoft.chat.entity.User;
import com.simbirsoft.chat.exceptions.UserNotFoundException;
import com.simbirsoft.chat.exceptions.UserOfflineException;
import com.simbirsoft.chat.repository.MessageRepository;
import com.simbirsoft.chat.repository.UserRepository;
import com.simbirsoft.chat.util.UserUtil;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class ChatSendMessageService extends AbstractChatService {
    private final SimpUserRegistry userRegistry;

    private final MessageRepository messageRepository;

    public ChatSendMessageService(
            SimpUserRegistry userRegistry,
            UserRepository userRepository,
            MessageRepository messageRepository
    ) {
        super(userRepository);
        this.userRegistry = userRegistry;
        this.messageRepository = messageRepository;
    }

    public void send(String receiverEmail, String content, Authentication auth)
            throws UserNotFoundException, UserOfflineException {
        User sender = UserUtil.retrieveUserFromAuth(auth);
        User receiver = retrieveUserByEmail(receiverEmail);

        if (!receiver.isBot() && userRegistry.getUser(receiverEmail) == null) {
            throw UserOfflineException.byEmail(receiverEmail);
        }

        messageRepository.save(new Message(content, sender, receiver));
    }
}

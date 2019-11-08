package com.simbirsoft.chat.service.chat;

import com.simbirsoft.chat.entity.User;
import com.simbirsoft.chat.exceptions.UserNotFoundException;
import com.simbirsoft.chat.repository.UserRepository;

import java.util.Optional;

public abstract class AbstractChatService {
    protected final UserRepository userRepository;

    public AbstractChatService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    protected User retrieveUserByEmail(String email) throws UserNotFoundException {
        Optional<User> mbUser = userRepository.findByEmail(email);
        if (!mbUser.isPresent()) {
            throw UserNotFoundException.byEmail(email);
        }
        return mbUser.get();
    }
}

package com.simbirsoft.chat.service.ws;

import com.simbirsoft.chat.dto.OnlineUserDTO;
import com.simbirsoft.chat.entity.User;
import com.simbirsoft.chat.exceptions.UserNotFoundException;
import com.simbirsoft.chat.repository.UserRepository;
import com.simbirsoft.chat.service.auth.UserContainer;
import com.simbirsoft.chat.service.user.avatar.AvatarPathNamingStrategy;
import com.simbirsoft.chat.util.UserUtil;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class OnlineUsersService {
    private final SimpUserRegistry userRegistry;

    private final UserRepository userRepository;

    private final AvatarPathNamingStrategy avatarPathNamingStrategy;

    public OnlineUsersService(
            SimpUserRegistry userRegistry,
            UserRepository userRepository,
            AvatarPathNamingStrategy avatarPathNamingStrategy
    ) {
        this.userRegistry = userRegistry;
        this.userRepository = userRepository;
        this.avatarPathNamingStrategy = avatarPathNamingStrategy;
    }

    /**
     * @param auth Current authentication session.
     * @return Result collection which contains all online users and all bots
     * registered in the system. Notice that the result is list which sorted
     * by {@link OnlineUserDTO}.
     * @throws UserNotFoundException In case when can not map simp user session
     *                               to the user entity.
     * @see OnlineUserDTO#compareTo(OnlineUserDTO)
     */
    public List<OnlineUserDTO> online(Authentication auth) throws UserNotFoundException {
        User authUser = UserUtil.retrieveUserFromAuth(auth);
        List<OnlineUserDTO> users = new ArrayList<>();

        // Iterates over online users
        for (SimpUser simpUser : userRegistry.getUsers()) {
            String email = simpUser.getName();
            if (authUser.getEmail().equals(email)) {
                continue;
            }
            Optional<User> mbUser = userRepository.findByEmail(email);
            if (!mbUser.isPresent()) {
                throw UserNotFoundException.byEmail(email);
            }

            users.add(createDTO(mbUser.get()));
        }

        // Iterates over bots
        for (User bot : userRepository.findAllBots()) {
            users.add(createDTO(bot));
        }

        Collections.sort(users);

        return users;
    }

    public OnlineUserDTO me(String email) throws UserNotFoundException {
        Optional<User> mbUser = userRepository.findByEmail(email);
        if (!mbUser.isPresent()) {
            throw UserNotFoundException.byEmail(email);
        }

        return createDTO(mbUser.get());
    }

    public OnlineUserDTO me(Authentication auth) {
        User user = ((UserContainer) auth.getPrincipal()).getUser();

        return createDTO(user);
    }

    private OnlineUserDTO createDTO(User user) {
        return new OnlineUserDTO(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                avatarPathNamingStrategy.relativeUrlString(user)
        );
    }
}

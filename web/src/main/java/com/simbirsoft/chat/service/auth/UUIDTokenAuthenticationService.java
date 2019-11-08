package com.simbirsoft.chat.service.auth;

import com.simbirsoft.chat.entity.Token;
import com.simbirsoft.chat.entity.User;
import com.simbirsoft.chat.exceptions.AccessDeniedException;
import com.simbirsoft.chat.exceptions.UserNotFoundException;
import com.simbirsoft.chat.repository.UserRepository;
import com.simbirsoft.chat.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of {@link UserTokenAuthenticationService} that uses UUID as token.
 *
 * @link https://en.wikipedia.org/wiki/Universally_unique_identifier
 */
@Service
public class UUIDTokenAuthenticationService implements UserTokenAuthenticationService {
    private static final int TOKEN_LIFETIME_DAYS = 7;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UUIDTokenAuthenticationService(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String login(String email, String password) throws UserNotFoundException {
        Optional<User> mbUser = userRepository.findByEmail(email);
        User user = mbUser.orElseThrow(() -> new UserNotFoundException("Bad email or password"));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new UserNotFoundException("Bad email or password");
        }

        if (!user.isBot()) {
            throw new AccessDeniedException("Only for bots");
        }

        String uuid = UUID.randomUUID().toString();
        Token token = new Token(uuid, DateUtil.addDays(TOKEN_LIFETIME_DAYS));
        user.setToken(token);
        userRepository.save(user);

        return uuid;
    }

    @Override
    public Optional<User> findByToken(String token) {
        return userRepository.findByTokenNonExpired(token);
    }
}

package com.simbirsoft.chat.service.auth;

import com.simbirsoft.chat.entity.User;
import com.simbirsoft.chat.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class RepositoryUserDetailsService implements UserDetailsService {
    private final UserRepository repository;

    public RepositoryUserDetailsService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // In our case username is email
        Optional<User> mbUser = repository.findByEmail(username);
        if (!mbUser.isPresent()) {
            throw new UsernameNotFoundException(String.format("User with email \"%s\" not found", username));
        }

        return new UserContainer(mbUser.get());
    }
}

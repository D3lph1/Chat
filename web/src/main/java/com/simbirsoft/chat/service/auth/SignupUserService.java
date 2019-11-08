package com.simbirsoft.chat.service.auth;

import com.simbirsoft.chat.dto.DTOtoEntityConverter;
import com.simbirsoft.chat.dto.SignupUserDTO;
import com.simbirsoft.chat.entity.User;
import com.simbirsoft.chat.exceptions.UserAlreadyExistsException;
import com.simbirsoft.chat.exceptions.UserInitializationException;
import com.simbirsoft.chat.repository.UserRepository;
import com.simbirsoft.chat.service.user.UserInitializer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * It defines transaction script that handles user signup request.
 */
@Service
@Transactional
public class SignupUserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserInitializer userInitializer;

    public SignupUserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserInitializer userInitializer) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userInitializer = userInitializer;
    }

    public void signup(SignupUserDTO userDTO) throws UserAlreadyExistsException, UserInitializationException {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw UserAlreadyExistsException.byEmail(userDTO.getEmail());
        }
        String hashedPassword = passwordEncoder.encode(userDTO.getPassword());
        User user = DTOtoEntityConverter.signup(userDTO, hashedPassword);
        userRepository.save(user);
        userInitializer.init(user);
    }
}

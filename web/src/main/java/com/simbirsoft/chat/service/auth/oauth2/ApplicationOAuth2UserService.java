package com.simbirsoft.chat.service.auth.oauth2;

import com.simbirsoft.chat.entity.User;
import com.simbirsoft.chat.exceptions.UserInitializationException;
import com.simbirsoft.chat.repository.UserRepository;
import com.simbirsoft.chat.service.auth.UserContainer;
import com.simbirsoft.chat.service.user.UserInitializer;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class ApplicationOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    private final UserInitializer userInitializer;

    public ApplicationOAuth2UserService(UserRepository userRepository, UserInitializer userInitializer) {
        this.userRepository = userRepository;
        this.userInitializer = userInitializer;
    }

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        GoogleOAuth2UserInfo oAuth2UserInfo = new GoogleOAuth2UserInfo(oAuth2User.getAttributes());

        Optional<User> mbUser = userRepository.findByEmail(oAuth2UserInfo.getEmail());
        User user;
        if (mbUser.isPresent()) {
            user = mbUser.get();
        } else {
            user = new User(
                    oAuth2UserInfo.getFirstName(),
                    oAuth2UserInfo.getLastName(),
                    oAuth2UserInfo.getEmail(),
                    null
            );
            try {
                userInitializer.init(user);
            } catch (UserInitializationException e) {
                throw new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR), e);
            }
            userRepository.save(user);
        }

        return new UserContainer(user, oAuth2User.getAttributes());
    }
}

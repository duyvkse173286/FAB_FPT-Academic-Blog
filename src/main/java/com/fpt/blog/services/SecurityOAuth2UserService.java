package com.fpt.blog.services;

import com.fpt.blog.configurations.SecurityUser;
import com.fpt.blog.constants.BaseConstants;
import com.fpt.blog.entities.User;
import com.fpt.blog.enums.Role;
import com.fpt.blog.enums.UserStatus;
import com.fpt.blog.repositories.UserRepository;
import com.fpt.blog.utils.ApplicationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SecurityOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // handle google lgoin
        OAuth2User oAuth2User =  super.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");

        // validate fpt email
        // check email
        if (!ApplicationUtils.isAllowedEmail(email)) {
            throw new UsernameNotFoundException(String.format( "Email is not allowed to access this system. Only emails with domain %s allowed", String.join(", ", BaseConstants.ALLOWED_DOMAINS)));
        }

        User user = userRepository.findByEmail(email)
                .orElse(null);

        if (user != null && !UserStatus.ACTIVE.equals(user.getStatus())) {
            throw new UsernameNotFoundException("User is not active");
        }

        if (user == null) {
            User newUser = new User();
            newUser.setName(oAuth2User.getAttribute("name"));
            newUser.setEmail(email);
            newUser.setAvatar(oAuth2User.getAttribute("picture"));
            newUser.setRole(Role.STUDENT);
            newUser.setStatus(UserStatus.ACTIVE);

            user =  userRepository.save(newUser);
        }


        return new SecurityUser(user, oAuth2User.getAttributes());
    }

}

package com.fpt.blog.services.impl;

import com.fpt.blog.configurations.SecurityUser;
import com.fpt.blog.entities.User;
import com.fpt.blog.enums.Role;
import com.fpt.blog.mappings.UserMapper;
import com.fpt.blog.models.user.request.CreateUserRequest;
import com.fpt.blog.models.user.response.UserResponse;
import com.fpt.blog.repositories.UserRepository;
import com.fpt.blog.services.UserService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    /**
     * Using email as username
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userByUsername = userRepository.findByEmail(username);
        if (userByUsername.isEmpty()) {
            log.error("Could not find user with that email: {}", username);
            throw new UsernameNotFoundException("Invalid credentials!");
        }
        User user = userByUsername.get();
        if (!user.getEmail().equals(username)) {
            log.error("Could not find user with that email: {}", username);
            throw new UsernameNotFoundException("Invalid credentials!");
        }

        return new SecurityUser(user);
    }


    @Override
    public boolean checkExistUser(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    @SneakyThrows
    public UserResponse createUser(CreateUserRequest request, Role role) {
        if (checkExistUser(request.getEmail())) {
            throw new Exception("Existed email");
        }

        User user = userMapper.toEntity(request);
        user.setRole(role);

        User savedUser = userRepository.save(user);
        log.info("Create new user: " + user.getEmail());

        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse getLoginUser() {
       try {
           User user = userRepository
                   .findLoginUser()
                   .orElse(null);

           if (user == null) {
               return null;
           }

           return userMapper.toResponse(user);
       } catch (Exception ex) {
           log.error(ex.getMessage());
           return null;
       }
    }
}

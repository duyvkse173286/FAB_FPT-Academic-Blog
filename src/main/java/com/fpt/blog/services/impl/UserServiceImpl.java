package com.fpt.blog.services.impl;

import com.fpt.blog.configurations.SecurityUser;
import com.fpt.blog.constants.BaseConstants;
import com.fpt.blog.entities.Following;
import com.fpt.blog.entities.User;
import com.fpt.blog.enums.Role;
import com.fpt.blog.enums.UserStatus;
import com.fpt.blog.mappings.UserMapper;
import com.fpt.blog.models.user.request.CreateUserRequest;
import com.fpt.blog.models.user.request.GetAllUsersRequest;
import com.fpt.blog.models.user.request.UpdateProfileRequest;
import com.fpt.blog.models.user.request.UpdateUserRequest;
import com.fpt.blog.models.user.response.UserResponse;
import com.fpt.blog.repositories.FollowingRepository;
import com.fpt.blog.repositories.UserRepository;
import com.fpt.blog.services.FileService;
import com.fpt.blog.services.UserService;
import com.fpt.blog.utils.ApplicationUtils;
import com.fpt.blog.utils.UploadMedia;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private FollowingRepository followingRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FileService fileService;

    /**
     * Using email as username
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    @SneakyThrows
    public UserDetails loadUserByUsername(String username) {
        // validate fpt email
        // check email
        if (!ApplicationUtils.isAllowedEmail(username)) {
            throw new AuthenticationServiceException(String.format( "Email is not allowed to access this system. Only emails with domain %s allowed", String.join(", ", BaseConstants.ALLOWED_DOMAINS)));
        }

        Optional<User> userByUsername = userRepository.findByEmail(username);
        User user = userByUsername.orElse(null);

        if (user == null || !user.getEmail().equals(username)) {
            log.error("Could not find user with that email: {}", username);
            throw new AuthenticationServiceException("Email is not existed!");
        }

        if (!UserStatus.ACTIVE.equals(user.getStatus())) {
            throw new AuthenticationServiceException("User is not active");
        }

        return new SecurityUser(user);
    }


    @Override
    public boolean checkExistUser(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    @SneakyThrows
    public UserResponse createUser(CreateUserRequest request) {
        if (checkExistUser(request.getEmail())) {
            throw new Exception("Existed email");
        }

        User user = userMapper.toEntity(request);
        user.setRole(
                request.getRole() != null
                        ? request.getRole()
                        : Role.STUDENT
        );

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

    @Override
    public UserResponse getUser(long userId) {
        User user = userRepository.findById(userId).orElse(null);
        return userMapper.toResponse(user);
    }

    @Override
    @SneakyThrows
    @Transactional
    public void unfollow(long userId) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new Exception("User not found"));

        User loggedUser = userRepository.findLoginUser()
                .orElseThrow(() -> new Exception("Unauthorized"));

        if (user.getId() == loggedUser.getId()) {
            return;
        }

        // check exist
        Following follow = followingRepository
                .findFirstByUserIdAndFollowerId(user.getId(), loggedUser.getId())
                .orElse(null);

        if (follow == null) {
            return;
        }

        user.getFollowers().remove(follow);
        userRepository.save(user);
    }

    @Override
    @SneakyThrows
    @Transactional
    public void follow(long userId) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new Exception("User not found"));

        User loggedUser = userRepository.findLoginUser()
                .orElseThrow(() -> new Exception("Unauthorized"));

        if (user.getId() == loggedUser.getId()) {
            return;
        }

        // check exist
        Following follow = followingRepository
                .findFirstByUserIdAndFollowerId(user.getId(), loggedUser.getId())
                .orElse(null);

        if (follow != null) {
            return;
        }

        follow = new Following()
                .setFollower(loggedUser)
                .setUser(user);

        user.getFollowers().add(follow);
        userRepository.save(user);
    }

    @Override
    public List<UserResponse> getFollowers(long userId) {
        return followingRepository.findByUserId(userId)
                .stream()
                .map(Following::getFollower)
                .map(userMapper::toResponse)
                .toList();
    }

    @Override
    public List<UserResponse> getFollowings(long userId) {
        return followingRepository.findByFollowerId(userId)
                .stream()
                .map(Following::getUser)
                .map(userMapper::toResponse)
                .toList();
    }

    @Override
    @SneakyThrows
    @Transactional
    public UserResponse updateProfile(UpdateProfileRequest request) {
        User loggedUser = userRepository.findLoginUser()
                .orElseThrow(() -> new Exception("Unauthorized"));

        loggedUser
                .setName(request.getName().trim())
                .setPhoneNumber(request.getPhoneNumber().trim())
                .setDob(request.getDob())
                .setDescription(request.getDescription().trim());

        // upload avatar
        if (!request.getAvatar().isEmpty()) {
            UploadMedia uploadMedia = fileService.uploadFile(request.getAvatar());
            loggedUser.setAvatar(uploadMedia.getUrl());
        }

        User savedUser = userRepository.save(loggedUser);

        // update authentication
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Authentication newAuth = new UsernamePasswordAuthenticationToken(new SecurityUser(savedUser), auth.getCredentials(), auth.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(newAuth);

        return userMapper.toResponse(savedUser);
    }

    @Override
    @SneakyThrows
    @Transactional
    public void updatePassword(String password) {
        User loggedUser = userRepository.findLoginUser()
                .orElseThrow(() -> new Exception("Unauthorized"));

        loggedUser.setPassword(passwordEncoder.encode(password));
        userRepository.save(loggedUser);
    }

    @Override
    public List<UserResponse> getAllUsers(GetAllUsersRequest request) {
        return userRepository.findAll(request.getSpecification(), request.getOrder())
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    @Override
    public Page<UserResponse> getAllUsersFitlerPaging(GetAllUsersRequest request) {
        return userRepository
                .findAll(request.getSpecification(), request.getPageable())
                .map(userMapper::toResponse);
    }

    @Override
    @SneakyThrows
    public UserResponse updateStatus(long userId, UserStatus status) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new Exception("User not found"));

        user.setStatus(status);

        userRepository.save(user);

        return userMapper.toResponse(user);
    }

    @Override
    @SneakyThrows
    @Transactional
    public UserResponse updateUser(long userId, UpdateUserRequest request) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new Exception("User not found"));

        user.setName(request.getName());
        user.setRole(request.getRole());
        user.setPhoneNumber(request.getPhoneNumber());

        if (StringUtils.isNotBlank(request.getPassword())) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        User savedUser = userRepository.save(user);

        return userMapper.toResponse(savedUser);
    }

}

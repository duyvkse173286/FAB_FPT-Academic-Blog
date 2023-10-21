package com.fpt.blog.services;

import com.fpt.blog.entities.User;
import com.fpt.blog.enums.Role;
import com.fpt.blog.enums.UserStatus;
import com.fpt.blog.models.user.request.CreateUserRequest;
import com.fpt.blog.models.user.request.GetAllUsersRequest;
import com.fpt.blog.models.user.request.UpdateProfileRequest;
import com.fpt.blog.models.user.request.UpdateUserRequest;
import com.fpt.blog.models.user.response.UserResponse;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.List;
import java.util.Map;

public interface UserService extends UserDetailsService {

    boolean checkExistUser(String email);

    UserResponse createUser(CreateUserRequest request);

    UserResponse getLoginUser();

    UserResponse getUser(long userId);

    void unfollow(long userId);

    void follow(long userId);

    List<UserResponse> getFollowers(long userId);

    List<UserResponse> getFollowings(long userId);

    UserResponse updateProfile(UpdateProfileRequest request);

    void updatePassword(String password);

    List<UserResponse> getAllUsers(GetAllUsersRequest request);

    UserResponse updateStatus(long userId, UserStatus status);

    UserResponse updateUser(long userId, UpdateUserRequest request);

}

package com.fpt.blog.services;

import com.fpt.blog.enums.Role;
import com.fpt.blog.models.user.request.CreateUserRequest;
import com.fpt.blog.models.user.response.UserResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    boolean checkExistUser(String email);

    UserResponse createUser(CreateUserRequest request, Role role);

    UserResponse getLoginUser();

}

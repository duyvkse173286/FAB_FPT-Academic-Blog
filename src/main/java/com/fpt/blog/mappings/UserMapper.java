package com.fpt.blog.mappings;

import com.fpt.blog.entities.User;
import com.fpt.blog.models.user.request.CreateUserRequest;
import com.fpt.blog.models.user.response.UserResponse;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {
    User toEntity(CreateUserRequest request);

    UserResponse toResponse(User user);

}

package com.fpt.blog.mappings;

import com.fpt.blog.entities.User;
import com.fpt.blog.models.user.request.CreateUserRequest;
import com.fpt.blog.models.user.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface UserMapper {

    @Mapping(source = "password", target = "password", qualifiedByName = "passwordMapper")
    User toEntity(CreateUserRequest request);

    UserResponse toResponse(User user);

    @Named("passwordMapper")
    static String passwordMapper(String password) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }
}

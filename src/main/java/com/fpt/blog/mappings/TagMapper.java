package com.fpt.blog.mappings;

import com.fpt.blog.entities.Tag;
import com.fpt.blog.entities.User;
import com.fpt.blog.models.tag.TagResponse;
import com.fpt.blog.models.user.request.CreateUserRequest;
import com.fpt.blog.models.user.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper
public interface TagMapper {

    TagResponse toResponse(Tag tag);
}

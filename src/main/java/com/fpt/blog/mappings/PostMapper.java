package com.fpt.blog.mappings;

import com.fpt.blog.entities.Category;
import com.fpt.blog.entities.Post;
import com.fpt.blog.entities.User;
import com.fpt.blog.models.category.response.CategoryResponse;
import com.fpt.blog.models.post.response.PostResponse;
import com.fpt.blog.models.user.response.UserResponse;
import org.mapstruct.Mapper;

@Mapper
public interface PostMapper {

    PostResponse toResponse(Post post);

    CategoryResponse toResponse(Category category);

    UserResponse toResponse(User user);

}

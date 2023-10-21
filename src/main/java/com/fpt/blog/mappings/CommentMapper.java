package com.fpt.blog.mappings;

import com.fpt.blog.entities.Comment;
import com.fpt.blog.models.comment.response.CommentResponse;
import org.mapstruct.Mapper;

@Mapper
public interface CommentMapper {

    CommentResponse toResponse(Comment comment);

}

package com.fpt.blog.models.comment.response;

import com.fpt.blog.models.user.response.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {

    private long id;

    private String content;

    private UserResponse user;

    private CommentResponse parent;

    private LocalDateTime createdAt;

}

package com.fpt.blog.models.post.response;

import com.fpt.blog.entities.Tag;
import com.fpt.blog.enums.PostStatus;
import com.fpt.blog.models.category.response.CategoryResponse;
import com.fpt.blog.models.user.response.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {

    private long id;

    private CategoryResponse category;

    private String title;

    private String content;

    private String description;

    private UserResponse user;

    private PostStatus status = PostStatus.WAITING;

    private LocalDateTime approvedAt;

    private LocalDateTime deniedAt;

    private String deniedReason;

    private Boolean commentEnabled;

    private int viewCount;

    private Set<Tag> tags = new HashSet<>();

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


}

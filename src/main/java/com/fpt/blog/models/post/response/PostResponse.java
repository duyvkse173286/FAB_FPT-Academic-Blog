package com.fpt.blog.models.post.response;

import com.fpt.blog.enums.PostStatus;
import com.fpt.blog.models.adward.response.AwardResponse;
import com.fpt.blog.models.category.response.CategoryResponse;
import com.fpt.blog.models.reaction.response.ReactionResponse;
import com.fpt.blog.models.tag.TagResponse;
import com.fpt.blog.models.user.response.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {


    private long id;

    private CategoryResponse category;

    private String title;

    private String thumbnail;

    private String content;

    private String description;

    private UserResponse user;

    private PostStatus status;

    private LocalDateTime approvedAt;

    private LocalDateTime rejectedAt;

    private String rejectReason;

    private Boolean commentEnabled;

    private int viewCount;

    private Set<TagResponse> tags = new HashSet<>();

    private UserResponse reviewBy;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private List<AwardResponse> awards;

    private List<ReactionResponse> reactions;


}

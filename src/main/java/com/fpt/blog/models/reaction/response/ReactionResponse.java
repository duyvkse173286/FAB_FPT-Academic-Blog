package com.fpt.blog.models.reaction.response;

import com.fpt.blog.enums.ReactType;
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
public class ReactionResponse {

    private long id;

    private ReactType type;

    private UserResponse user;

    private LocalDateTime createdAt;

}

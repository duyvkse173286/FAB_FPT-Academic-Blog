package com.fpt.blog.models.post.request;

import com.fpt.blog.enums.ReactType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReactPostRequest {

    private long postId;

    private ReactType type;

}

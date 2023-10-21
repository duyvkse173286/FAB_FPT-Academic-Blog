package com.fpt.blog.models.post.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApprovePostRequest {

    private long postId;

    private List<Long> awardIds = new ArrayList<>();
}

package com.fpt.blog.models.comment.request;/*  Welcome to Jio word
    @author: Jio
    Date: 10/28/2023
    Time: 12:05 PM
    
    ProjectName: fpt-blog
    Jio: I wish you always happy with coding <3
*/

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteCommentRequest {

    private long commentId;

    private String reason;
}

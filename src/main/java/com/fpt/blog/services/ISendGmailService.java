package com.fpt.blog.services;/*  Welcome to Jio word
    @author: Jio
    Date: 10/27/2023
    Time: 10:29 AM
    
    ProjectName: fpt-blog
    Jio: I wish you always happy with coding <3
*/

import com.fpt.blog.entities.Comment;
import com.fpt.blog.entities.User;
import com.fpt.blog.models.comment.request.DeleteCommentRequest;

public interface ISendGmailService {
    void sendMailBanCommentV1(User user, Comment commentBan, DeleteCommentRequest request);
}

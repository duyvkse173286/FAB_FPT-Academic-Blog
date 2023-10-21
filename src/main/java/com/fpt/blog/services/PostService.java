package com.fpt.blog.services;

import com.fpt.blog.entities.Post;
import com.fpt.blog.enums.ReactType;
import com.fpt.blog.models.comment.response.CommentResponse;
import com.fpt.blog.models.common.request.BaseFilterRequest;
import com.fpt.blog.models.post.request.*;
import com.fpt.blog.models.post.response.PostResponse;
import com.fpt.blog.models.reaction.response.ReactionResponse;

import java.util.List;

public interface PostService {

    Post createPost(CreatePostRequest request);

    List<PostResponse> getAllPosts(BaseFilterRequest<Post> request);

    PostResponse approvePost(long id, ApprovePostRequest request);

    PostResponse rejectPost(long id, RejectPostRequest request);

    PostResponse getPost(long id);

    PostResponse deletePost(long postId);

    PostResponse updatePost(long postId, UpdatePostRequest request);

    ReactionResponse react(long id, ReactType type);

    ReactionResponse removeReaction(long id, ReactType type);

    List<CommentResponse> getAllComments(long postId);

    CommentResponse commentPost(long id, CommentPostRequest request);

    CommentResponse deleteComment(long id);

    List<PostResponse> getFeaturedPosts(int top);
}

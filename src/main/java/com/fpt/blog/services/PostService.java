package com.fpt.blog.services;

import com.fpt.blog.entities.Post;
import com.fpt.blog.models.post.request.CreatePostRequest;
import com.fpt.blog.models.post.request.GetAllPostRequest;
import com.fpt.blog.models.post.response.PostResponse;

import java.util.List;

public interface PostService {

    Post createPost(CreatePostRequest request);

    List<PostResponse> getAllPosts(GetAllPostRequest request);


}

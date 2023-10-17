package com.fpt.blog.services.impl;

import com.fpt.blog.entities.Category;
import com.fpt.blog.entities.Post;
import com.fpt.blog.entities.Tag;
import com.fpt.blog.entities.User;
import com.fpt.blog.mappings.PostMapper;
import com.fpt.blog.models.post.request.CreatePostRequest;
import com.fpt.blog.models.post.request.GetAllPostRequest;
import com.fpt.blog.models.post.response.PostResponse;
import com.fpt.blog.repositories.CategoryRepository;
import com.fpt.blog.repositories.PostRepository;
import com.fpt.blog.repositories.TagRepository;
import com.fpt.blog.repositories.UserRepository;
import com.fpt.blog.services.PostService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final UserRepository userRepository;

    private final CategoryRepository categoryRepository;

    private final TagRepository tagRepository;

    private final PostRepository postRepository;

    private final PostMapper postMapper;


    @Override
    @Transactional
    @SneakyThrows
    public Post createPost(CreatePostRequest request) {
        Category category = categoryRepository
                .findById(request.getCategoryId())
                .orElseThrow(() -> new Exception("Category not found"));

        User author = userRepository.findLoginUser()
                .orElseThrow(() -> new Exception("Unauthorized"));


        Post post = new Post()
                .setTitle(request.getTitle())
                .setContent(request.getContent())
                .setCategory(category)
                .setUser(author);

        if (request.getTags() != null) {
            Set<Tag> tags = new HashSet<>();
            for(String t: request.getTags().split(",")) {
                t = t.trim().toLowerCase();
                Tag tag = tagRepository
                        .findFirstByName(t)
                        .orElse(new Tag(t));

                tag.getPosts().add(post);
                tags.add(tag);
            }
            post.setTags(tags);
        }

        Post savedPost = postRepository.save(post);

        return savedPost;
    }

    @Override
    public List<PostResponse> getAllPosts(GetAllPostRequest request) {
        return postRepository.findAll(request.getSpecification())
                .stream().map(postMapper::toResponse)
                .toList();
    }
}

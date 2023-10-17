package com.fpt.blog.controllers.mentor;

import com.fpt.blog.enums.PostStatus;
import com.fpt.blog.models.post.request.GetAllPostRequest;
import com.fpt.blog.models.post.response.PostResponse;
import com.fpt.blog.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mentor")
public class MentorController {

    private final PostService postService;

    @GetMapping("/post-requests")
    public String postRequest(Model model) {

        List<PostResponse> posts = postService.getAllPosts(new GetAllPostRequest().setStatus(PostStatus.WAITING));
        model.addAttribute("posts", posts);
        return "mentor/post-requests";
    }
}

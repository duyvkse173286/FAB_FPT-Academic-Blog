package com.fpt.blog.controllers;

import com.fpt.blog.enums.PostStatus;
import com.fpt.blog.models.category.request.GetAllCategoryRequest;
import com.fpt.blog.models.category.response.CategoryResponse;
import com.fpt.blog.models.post.request.GetAllPostRequest;
import com.fpt.blog.models.post.response.PostResponse;
import com.fpt.blog.models.tag.TagResponse;
import com.fpt.blog.models.user.response.UserResponse;
import com.fpt.blog.services.CategoryService;
import com.fpt.blog.services.PostService;
import com.fpt.blog.services.TagService;
import com.fpt.blog.services.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping({"/", "index", "home"})
public class HomeController {

    private final UserService userService;

    private final PostService postService;

    private final CategoryService categoryService;

    private final TagService tagService;

    @GetMapping("")
    public String homeController(Model model, HttpSession session) {
        UserResponse loggedUser = userService.getLoginUser();
        if (loggedUser != null) {
            session.setAttribute("loggedUser", loggedUser);
        }

        List<PostResponse> posts = postService.getAllPosts(new GetAllPostRequest()
                .setStatus(PostStatus.APPROVED));

        List<PostResponse> featuredPosts = postService.getFeaturedPosts(5);
        List<CategoryResponse> categories = categoryService.getAllCategories(null);
        List<TagResponse> tags = tagService.getAllTags();

        model.addAttribute("posts", posts);
        model.addAttribute("categories", categories);
        model.addAttribute("tags", tags);
        model.addAttribute("featuredPosts", featuredPosts);

        return "index";
    }
}

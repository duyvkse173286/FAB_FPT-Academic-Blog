package com.fpt.blog.controllers;

import com.fpt.blog.entities.Category;
import com.fpt.blog.entities.Post;
import com.fpt.blog.models.category.response.CategoryResponse;
import com.fpt.blog.models.post.request.CreatePostRequest;
import com.fpt.blog.services.CategoryService;
import com.fpt.blog.services.PostService;
import com.fpt.blog.services.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/posts")
@Slf4j
public class PostController {

    private final CategoryService categoryService;

    private final UserService userService;

    private final PostService postService;


    @GetMapping("create")
    public String createPostForm(Model model) {
        List<CategoryResponse> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "create-post";
    }

    @PostMapping("create")
    public String createPost(@ModelAttribute CreatePostRequest request, Model model, HttpSession session) {
        model.addAttribute("title", request.getTitle());
        model.addAttribute("categoryId", request.getCategoryId());
        model.addAttribute("tags", request.getTags());
        model.addAttribute("content", request.getContent());

        // validate
        try {
            if (userService.getLoginUser() == null) {
                return "redirect:/auth/login";
            }

            // create post
            if (categoryService.getCategory(request.getCategoryId()) == null) {
                session.setAttribute("error", "Category not found");
            }

            Post savedPost = postService.createPost(request);
            if (savedPost != null) {
                session.setAttribute("message", "Create post successfully. Wait for accepting");
                return "redirect:/home";
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }

        session.setAttribute("error", "Internal server error");
        return "create-post";
    }


}

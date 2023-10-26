package com.fpt.blog.controllers.mentor;

import com.fpt.blog.entities.Award;
import com.fpt.blog.enums.PostStatus;
import com.fpt.blog.models.adward.request.GetAllAwardRequest;
import com.fpt.blog.models.adward.response.AwardResponse;
import com.fpt.blog.models.post.request.ApprovePostRequest;
import com.fpt.blog.models.post.request.GetAllPostRequest;
import com.fpt.blog.models.post.request.RejectPostRequest;
import com.fpt.blog.models.post.response.PostResponse;
import com.fpt.blog.services.AwardService;
import com.fpt.blog.services.PostService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mentor")
public class MentorController {

    private final PostService postService;

    private final AwardService awardService;

    @GetMapping("/post-requests")
    public String postRequest(Model model, @ModelAttribute GetAllPostRequest request) {

        request.setStatus(PostStatus.WAITING);
        Page<PostResponse> postPage = postService.getAllPosts(request);
        List<AwardResponse> awards = awardService.getAlAwards(new GetAllAwardRequest());

        model.addAttribute("posts", postPage.getContent());
        model.addAttribute("awards", awards);
        model.addAttribute("pageNumber", postPage.getNumber() + 1);
        model.addAttribute("totalPages", postPage.getTotalPages());

        model.addAttribute(
                "queryString",
                String.format("/mentor/post-requests?search=%s&tag=%s&categoryId=%s",
                        Objects.requireNonNullElse(request.getSearch(), ""),
                        Objects.requireNonNullElse(request.getTag(), ""),
                        Objects.requireNonNullElse(request.getCategoryId(), "")));

        return "mentor/post-requests";
    }

    @PostMapping("/approve-post")
    public String approvePostRequest(@ModelAttribute ApprovePostRequest request, HttpSession session) {
        try {
            PostResponse post = postService.getPost(request.getPostId());
            if (post == null) {
                session.setAttribute("error", "Post not found");
                return "redirect:/mentor/post-requests";
            }
            postService.approvePost(post.getId(), request);
        } catch (Exception ex) {
            session.setAttribute("error", ex.getMessage());
        }
        return "redirect:/mentor/post-requests";
    }

    @PostMapping("/reject-post")
    public String rejectPostRequest(@ModelAttribute RejectPostRequest request, HttpSession session) {
        try {
            PostResponse post = postService.getPost(request.getPostId());
            if (post == null) {
                session.setAttribute("error", "Post not found");
                return "redirect:/mentor/post-requests";
            }
            postService.rejectPost(post.getId(), request);
        } catch (Exception ex) {
            session.setAttribute("error", ex.getMessage());
        }
        return "redirect:/mentor/post-requests";
    }
}

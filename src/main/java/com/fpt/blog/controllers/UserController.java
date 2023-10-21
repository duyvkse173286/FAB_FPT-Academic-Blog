package com.fpt.blog.controllers;

import com.fpt.blog.constants.BaseConstants;
import com.fpt.blog.enums.PostStatus;
import com.fpt.blog.models.post.request.GetAllPostRequest;
import com.fpt.blog.models.post.response.PostResponse;
import com.fpt.blog.models.user.request.UpdatePasswordRequest;
import com.fpt.blog.models.user.request.UpdateProfileRequest;
import com.fpt.blog.models.user.response.UserResponse;
import com.fpt.blog.services.PostService;
import com.fpt.blog.services.UserService;
import com.fpt.blog.utils.RegexUtils;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.hibernate.sql.Update;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final PostService postService;

    @GetMapping("{userId}")
    public String getUserDetails(@PathVariable long userId, Model model) {
        UserResponse user = userService.getUser(userId);
        if (user == null) {
            return "not-found";
        }

        model.addAttribute("user", user);

        // posts
        List<PostResponse> posts = postService.getAllPosts(new GetAllPostRequest()
                .setUserId(userId)
                .setStatus(PostStatus.APPROVED));

        List<UserResponse> followers = userService.getFollowers(userId);
        List<UserResponse> followings = userService.getFollowings(userId);

        model.addAttribute("posts", posts);
        model.addAttribute("followers", followers);
        model.addAttribute("followings", followings);

        UserResponse loggedUser = userService.getLoginUser();
        if (loggedUser != null) {
            model.addAttribute("followed", followers.stream().anyMatch(f -> f.getId() == loggedUser.getId()));
        }

        return "user-detail";
    }

    @PostMapping("{userId}/follow")
    public String follow(@PathVariable long userId, Model model, HttpSession session) {
        try {
            UserResponse user = userService.getUser(userId);
            if (user == null) {
                return "not-found";
            }

            userService.follow(userId);

        } catch (Exception ex) {
            session.setAttribute("error", ex.getMessage());
        }

        return "redirect:/users/" + userId;
    }


    @PostMapping("{userId}/un-follow")
    public String unfollow(@PathVariable long userId, Model model, HttpSession session) {
        try {
            UserResponse user = userService.getUser(userId);
            if (user == null) {
                return "not-found";
            }

            userService.unfollow(userId);

        } catch (Exception ex) {
            session.setAttribute("error", ex.getMessage());
        }

        return "redirect:/users/" + userId;
    }

    @GetMapping("profile")
    public String getProfile(Model model) {
        UserResponse user = userService.getLoginUser();
        if (user == null) {
            return "redirect:/auth/login";
        }

        model.addAttribute("user", user);

        // posts
        List<PostResponse> posts = postService.getAllPosts(new GetAllPostRequest().setUserId(user.getId()));
        List<UserResponse> followers = userService.getFollowers(user.getId());
        List<UserResponse> followings = userService.getFollowings(user.getId());

        model.addAttribute("posts", posts);
        model.addAttribute("followers", followers);
        model.addAttribute("followings", followings);

        UserResponse loggedUser = userService.getLoginUser();

        return "profile";
    }

    @PostMapping("profile")
    public String updateProfile(@ModelAttribute UpdateProfileRequest request, HttpSession httpSession) {
        try {
            // validate
              if (request.getPhoneNumber() != null
                    && !request.getPhoneNumber().isBlank()
                    && !RegexUtils.isValidPhoneNumber(request.getPhoneNumber())) {
                httpSession.setAttribute("error", "Invalid phone number format");
                return "redirect:/users/profile";
            }

            // update profile
            userService.updateProfile(request);
            httpSession.setAttribute("message", "Update profile successfully");

        } catch (Exception ex) {
            httpSession.setAttribute("error", ex.getMessage());
        }
        return "redirect:/users/profile";

    }

    @PostMapping("password")
    public String updatePassword(@ModelAttribute UpdatePasswordRequest request, HttpSession session) {
        try {

            if (request.getConfirmPassword() == null || !request.getConfirmPassword().equals(request.getPassword())) {
                session.setAttribute("error", "Confirm password not match");
                return "redirect:/users/profile";
            }

            if (request.getPassword() == null || request.getPassword().length() < BaseConstants.MIN_PASSWORD_LENGTH) {
                session.setAttribute("error", String.format("Password must contain at least %d characters", BaseConstants.MIN_PASSWORD_LENGTH));
                return "redirect:/users/profile";
            }

            userService.updatePassword(request.getPassword());
            session.setAttribute("message", "Update password successfully");

        } catch (Exception ex) {

        }
        return "redirect:/users/profile";
    }

}

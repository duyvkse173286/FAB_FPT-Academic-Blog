package com.fpt.blog.controllers.admin;

import com.fpt.blog.constants.BaseConstants;
import com.fpt.blog.enums.UserStatus;
import com.fpt.blog.models.user.request.CreateUserRequest;
import com.fpt.blog.models.user.request.GetAllUsersRequest;
import com.fpt.blog.models.user.request.UpdateUserRequest;
import com.fpt.blog.models.user.response.UserResponse;
import com.fpt.blog.services.UserService;
import com.fpt.blog.utils.ApplicationUtils;
import com.fpt.blog.utils.RegexUtils;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Objects;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin/users")
public class AdminUserController {

    private final UserService userService;

    @GetMapping
    public String getAllUser(@ModelAttribute GetAllUsersRequest request, Model model) {
        Page<UserResponse> users = userService.getAllUsersFitlerPaging(request);

        model.addAttribute("users", users.getContent());

        // filter request
        model.addAttribute("search", request.getSearch());
        model.addAttribute("role", request.getRole());
        model.addAttribute("status", request.getStatus());
        model.addAttribute("pageNumber", users.getNumber() + 1);
        model.addAttribute("totalPages", users.getTotalPages());

        model.addAttribute(
                "queryString",
                String.format(
                        "/admin/users?search=%s&role=%s&status=%s",
                        Objects.requireNonNullElse(request.getSearch(), ""),
                        Objects.requireNonNullElse(request.getRole(), ""),
                        Objects.requireNonNullElse(request.getStatus(), "")));

        return "admin/users";
    }

    @PostMapping
    public String createUser(@ModelAttribute CreateUserRequest request, HttpSession session, Model model, RedirectAttributes redirectAttrs) {
        try {
            redirectAttrs.addFlashAttribute("createRequest", request);

            // check email
            if (!ApplicationUtils.isAllowedEmail(request.getEmail().trim())) {
                session.setAttribute("error", String.format("Email is not allowed to access this system. Only emails with domain %s allowed", String.join(", ", BaseConstants.ALLOWED_DOMAINS)));
                return "redirect:/admin/users";
            }

            if (userService.checkExistUser(request.getEmail().trim())) {
                session.setAttribute("error", "Existed email");
                return "redirect:/admin/users";
            }

            // check password
            if (request.getPassword() == null || request.getPassword().length() < BaseConstants.MIN_PASSWORD_LENGTH) {
                session.setAttribute("error", String.format("Password must contain at least %d characters", BaseConstants.MIN_PASSWORD_LENGTH));
                return "redirect:/admin/users";
            }

            // check confirm password
            if (request.getConfirmPassword() == null || !request.getConfirmPassword().equals(request.getPassword())) {
                session.setAttribute("error", "Confirm password not match");
                return "redirect:/admin/users";
            }

//
            if (StringUtils.isNotBlank(request.getPhoneNumber())
                    && !RegexUtils.isValidPhoneNumber(request.getPhoneNumber())) {
                session.setAttribute("error", "Invalid phone number format");
                return "redirect:/admin/users";
            }


            UserResponse user = userService.createUser(request);
            if (user != null) {
                session.setAttribute("message", "Create user successfully");
            }
        } catch (Exception ex) {
            session.setAttribute("error", ex.getMessage());
        }
        return "redirect:/admin/users";
    }

    @PostMapping("{userId}/update")
    public String updateUser(@PathVariable("userId") long userId, @ModelAttribute UpdateUserRequest request, HttpSession session, Model model) {
        try {

            if (StringUtils.isNotBlank(request.getPhoneNumber())
                    && !RegexUtils.isValidPhoneNumber(request.getPhoneNumber())) {
                session.setAttribute("error", "Invalid phone number format");
                return "redirect:/admin/users";
            }

            if ((StringUtils.isNotBlank(request.getPassword()) && StringUtils.isBlank(request.getConfirmPassword()))) {
                session.setAttribute("error", "Confirm password is required");
                return "redirect:/admin/users";
            }

            // check password
            if (StringUtils.isNotBlank(request.getPassword()) && request.getPassword().length() < BaseConstants.MIN_PASSWORD_LENGTH) {
                session.setAttribute("error", String.format("Password must contain at least %d characters", BaseConstants.MIN_PASSWORD_LENGTH));
                return "redirect:/admin/users";
            }

            // check confirm password
            if ((StringUtils.isNotBlank(request.getPassword()) && StringUtils.isNotBlank(request.getConfirmPassword()))
                    && !request.getConfirmPassword().equals(request.getPassword())) {
                session.setAttribute("error", "Confirm password not match");
                return "redirect:/admin/users";
            }

            UserResponse user = userService.updateUser(userId, request);

            if (user != null) {
                session.setAttribute("message", "Update user successfully");
            }
        } catch (Exception ex) {
            session.setAttribute("error", ex.getMessage());
        }
        return "redirect:/admin/users";
    }


    @PostMapping("{userId}/status")
    public String updateStatus(@PathVariable long userId, @RequestParam UserStatus status, HttpSession session) {
        try {
            UserResponse user = userService.updateStatus(userId, status);
            if (user != null) {
                session.setAttribute("message", "Update user status successfully");
            }
        } catch (Exception ex) {
            session.setAttribute("error", ex.getMessage());
        }
        return "redirect:/admin/users";
    }

}

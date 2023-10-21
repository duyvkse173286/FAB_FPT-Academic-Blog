package com.fpt.blog.controllers;


import com.fpt.blog.constants.BaseConstants;
import com.fpt.blog.enums.Role;
import com.fpt.blog.models.auth.request.LoginRequest;
import com.fpt.blog.models.user.request.CreateUserRequest;
import com.fpt.blog.models.user.response.UserResponse;
import com.fpt.blog.services.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping({ "auth"})
public class AuthController {

    private final UserService userService;

    @GetMapping("login")
    public String login(Model model) {
        return "login";
    }

    @GetMapping("signup")
    public String signup(Model model) {
        return "signup";
    }


    @PostMapping("signup")
    public String signup(@ModelAttribute CreateUserRequest request, Model model, HttpSession session) {
        model.addAttribute("email", request.getEmail());
        model.addAttribute("name", request.getName());
        model.addAttribute("password", request.getPassword());
        model.addAttribute("confirmPassword", request.getConfirmPassword());

        try {
            // check email
            if (userService.checkExistUser(request.getEmail().trim())) {
                session.setAttribute("error", "Existed email");
                return "signup";
            }

            // check password
            if (request.getPassword() == null || request.getPassword().length() < BaseConstants.MIN_PASSWORD_LENGTH) {
                session.setAttribute("error", String.format("Password must contain at least %d characters", BaseConstants.MIN_PASSWORD_LENGTH));
                return "signup";
            }

            // check confirm password
            if (request.getConfirmPassword() == null || !request.getConfirmPassword().equals(request.getPassword())) {
                session.setAttribute("error", "Confirm password not match");
                return "signup";
            }

            // create user
            UserResponse userResponse = userService.createUser(request);

            // return login view
            session.setAttribute("message", "Sign up successfully");

            return "redirect:/auth/login";

        } catch (Exception ex) {
            session.setAttribute("error", "Sign up failed: " + ex.getMessage());
            return "signup";
        }
    }


}

package com.fpt.blog.controllers;

import com.fpt.blog.models.user.response.UserResponse;
import com.fpt.blog.services.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping({"", "index", "home"})
public class HomeController {

    private final UserService userService;

    @GetMapping("")
    public String homeController(Model model, HttpSession session) {
        UserResponse loggedUser = userService.getLoginUser();
        if (loggedUser != null) {
            session.setAttribute("loggedUser", loggedUser);
        }


        return "index";
    }
}

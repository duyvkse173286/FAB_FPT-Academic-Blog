package com.fpt.blog.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping({"", "index", "home"})
public class HomeController {

    @GetMapping("")
    public String homeController(Model model) {
        return "index";
    }
}

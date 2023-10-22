package com.fpt.blog.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@Slf4j
@ControllerAdvice
public class SettingControllerAdvice {

    @ModelAttribute
    public void handleRequest(HttpServletRequest request, Model model) {
        String requestURI = request.getRequestURI();

        //populating request URI in the model
        log.info("Request URI: " + requestURI);

        model.addAttribute("path", requestURI);
    }

}

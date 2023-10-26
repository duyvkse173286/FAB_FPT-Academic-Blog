package com.fpt.blog.utils;

import com.fpt.blog.models.category.request.GetAllCategoryRequest;
import com.fpt.blog.models.category.response.CategoryResponse;
import com.fpt.blog.services.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class CategoriesControllerAdvice {

    private final CategoryService categoryService;

    @ModelAttribute
    public void handleRequest(HttpServletRequest request, Model model) {
        List<CategoryResponse> categories = categoryService.getAllCategories(new GetAllCategoryRequest());
        model.addAttribute("nav_categories", categories);
    }

}

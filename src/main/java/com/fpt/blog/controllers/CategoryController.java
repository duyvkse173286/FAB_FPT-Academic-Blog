package com.fpt.blog.controllers;

import com.fpt.blog.models.category.request.GetAllCategoryRequest;
import com.fpt.blog.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public String categoriesPage(@RequestParam(name = "search", required = false) String search, Model model) {
        model.addAttribute("categories", categoryService.getAllCategories(new GetAllCategoryRequest()
                .setSearch(search)));
        model.addAttribute("cateSearch", search);

        return "categories";
    }

}

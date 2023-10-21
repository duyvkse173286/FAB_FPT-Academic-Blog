package com.fpt.blog.controllers.admin;

import com.fpt.blog.constants.BaseConstants;
import com.fpt.blog.enums.UserStatus;
import com.fpt.blog.models.category.request.CreateCategoryRequest;
import com.fpt.blog.models.category.request.GetAllCategoryRequest;
import com.fpt.blog.models.category.request.UpdateCategoryRequest;
import com.fpt.blog.models.category.response.CategoryResponse;
import com.fpt.blog.models.user.request.CreateUserRequest;
import com.fpt.blog.models.user.request.GetAllUsersRequest;
import com.fpt.blog.models.user.request.UpdateUserRequest;
import com.fpt.blog.models.user.response.UserResponse;
import com.fpt.blog.services.CategoryService;
import com.fpt.blog.services.UserService;
import com.fpt.blog.utils.RegexUtils;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin/categories")
public class AdminCategoryController {

    private final CategoryService categoryService;


    @GetMapping
    public String getAllCategories(@ModelAttribute GetAllCategoryRequest request, Model model) {
        List<CategoryResponse> categories = categoryService.getAllCategories(request);

        model.addAttribute("filter", request);
        model.addAttribute("categories", categories);

        return "admin/categories";
    }

    @PostMapping
    public String createCategory(@ModelAttribute CreateCategoryRequest request, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        try {

            redirectAttributes.addFlashAttribute("createRequest", request);
            CategoryResponse cate = categoryService.createCategory(request);
            if (cate != null) {
                session.setAttribute("message", "Create category successfully");
            }
        } catch (Exception ex) {
            session.setAttribute("error", ex.getMessage());
        }

        return "redirect:/admin/categories";
    }


    @PostMapping("{cateId}/update")
    public String createCategory(@PathVariable long cateId, @ModelAttribute UpdateCategoryRequest request, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            CategoryResponse cate = categoryService.getCategory(cateId);
            if (cate == null) {
                return "not-found";
            }

            redirectAttributes.addFlashAttribute("updateRequest", request);
            CategoryResponse updatedCate = categoryService.updateCategory(cateId, request);
            if (updatedCate != null) {
                session.setAttribute("message", "Update category successfully");
            }
        } catch (Exception ex) {
            session.setAttribute("error", ex.getMessage());
        }

        return "redirect:/admin/categories";
    }


    @PostMapping("{cateId}/delete")
    public String createCategory(@PathVariable long cateId, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            CategoryResponse cate = categoryService.getCategory(cateId);
            if (cate == null) {
                return "not-found";
            }

            CategoryResponse deletedCate = categoryService.removeCategory(cateId);
            if (deletedCate != null) {
                session.setAttribute("message", "Delete category successfully");
            }
        } catch (Exception ex) {
            session.setAttribute("error", ex.getMessage());
        }

        return "redirect:/admin/categories";
    }

}

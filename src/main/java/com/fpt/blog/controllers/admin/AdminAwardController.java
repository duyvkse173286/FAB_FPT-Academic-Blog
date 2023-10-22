package com.fpt.blog.controllers.admin;

import com.fpt.blog.models.adward.request.CreateAwardRequest;
import com.fpt.blog.models.adward.request.GetAllAwardRequest;
import com.fpt.blog.models.adward.request.UpdateAwardRequest;
import com.fpt.blog.models.adward.response.AwardResponse;
import com.fpt.blog.models.category.request.CreateCategoryRequest;
import com.fpt.blog.models.category.request.GetAllCategoryRequest;
import com.fpt.blog.models.category.request.UpdateCategoryRequest;
import com.fpt.blog.models.category.response.CategoryResponse;
import com.fpt.blog.services.AwardService;
import com.fpt.blog.services.CategoryService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin/awards")
public class AdminAwardController {

    private final AwardService awardService;

    @GetMapping
    public String getAllAwards(@ModelAttribute GetAllAwardRequest request, Model model) {
        List<AwardResponse> awards = awardService.getAlAwards(request);

        model.addAttribute("filter", request);
        model.addAttribute("awards", awards);

        return "admin/awards";
    }

    @PostMapping
    public String createAward(@ModelAttribute CreateAwardRequest request, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        try {

            redirectAttributes.addFlashAttribute("createRequest", request);
            AwardResponse award = awardService.createAward(request);
            if (award != null) {
                session.setAttribute("message", "Create award successfully");
            }
        } catch (Exception ex) {
            session.setAttribute("error", ex.getMessage());
        }

        return "redirect:/admin/awards";
    }


    @PostMapping("{awardId}/update")
    public String createCategory(@PathVariable long awardId, @ModelAttribute UpdateAwardRequest request, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            AwardResponse award = awardService.getAward(awardId);
            if (award == null) {
                return "not-found";
            }

            redirectAttributes.addFlashAttribute("updateRequest", request);
            AwardResponse updatedAward = awardService.updateAward(awardId, request);
            if (updatedAward != null) {
                session.setAttribute("message", "Update award successfully");
            }
        } catch (Exception ex) {
            session.setAttribute("error", ex.getMessage());
        }

        return "redirect:/admin/awards";
    }


    @PostMapping("{awardId}/delete")
    public String createCategory(@PathVariable long awardId, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            AwardResponse award = awardService.getAward(awardId);
            if (award == null) {
                return "not-found";
            }

            AwardResponse deletedAward = awardService.deleteAward(awardId);
            if (deletedAward != null) {
                session.setAttribute("message", "Delete award successfully");
            }
        } catch (Exception ex) {
            session.setAttribute("error", ex.getMessage());
        }

        return "redirect:/admin/awards";
    }

}

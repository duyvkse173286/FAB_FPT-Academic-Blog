package com.fpt.blog.controllers.admin;

import com.fpt.blog.models.tag.CreateTagRequest;
import com.fpt.blog.models.tag.GetAllTagRequest;
import com.fpt.blog.models.tag.TagResponse;
import com.fpt.blog.models.tag.UpdateTagRequest;
import com.fpt.blog.services.TagService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Objects;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin/tags")
public class AdminTagController {

    private final TagService tagService;


    @GetMapping
    public String getAllTags(@ModelAttribute GetAllTagRequest request, Model model) {
        Page<TagResponse> tags = tagService.getAllTagsFilterPaging(request);

        model.addAttribute("tags", tags.getContent());
        model.addAttribute("search", request.getSearch());
        model.addAttribute("pageNumber", tags.getNumber() + 1);
        model.addAttribute("totalPages", tags.getTotalPages());

        model.addAttribute(
                "queryString",
                String.format("/admin/tags?search=%s", Objects.requireNonNullElse(request.getSearch(), "")));

        return "admin/tags";
    }

    @PostMapping
    public String createTag(@ModelAttribute CreateTagRequest request, RedirectAttributes redirectAttributes, HttpSession session) {
        try {
            redirectAttributes.addFlashAttribute("createRequest", request);

            // check existed
            TagResponse existedTag = tagService.getTag(request.getName().toLowerCase());

            if (existedTag != null) {
                session.setAttribute("error", "Existed tag name");
                return "redirect:/admin/tags";
            }

            TagResponse tag = tagService.createTag(request.getName());
            if (tag != null) {
                session.setAttribute("message", "Create tag successfully");
            }
        } catch (Exception ex) {
            session.setAttribute("error", ex.getMessage());
        }

        return "redirect:/admin/tags";
    }

    @PostMapping("{tagId}/update")
    public String updateTag(@PathVariable long tagId, @ModelAttribute UpdateTagRequest request, RedirectAttributes redirectAttributes, HttpSession session) {
        try {

            TagResponse tag = tagService.updateTag(tagId, request.getName());

            if (tag != null) {
                session.setAttribute("message", "Update tag successfully");
            }

        } catch (Exception ex) {
            session.setAttribute("error", ex.getMessage());
        }

        return "redirect:/admin/tags";
    }

    @PostMapping("{tagId}/delete")
    public String deleteTag(@PathVariable long tagId, RedirectAttributes redirectAttributes, HttpSession session) {
        try {
            TagResponse tag = tagService.removeTag(tagId);
            if (tag != null) {
                session.setAttribute("message", "Delete tag successfully");
            }
        } catch (Exception ex) {
            session.setAttribute("error", ex.getMessage());
        }

        return "redirect:/admin/tags";
    }

}

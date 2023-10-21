package com.fpt.blog.controllers;

import com.fpt.blog.entities.Post;
import com.fpt.blog.enums.PostStatus;
import com.fpt.blog.enums.ReactType;
import com.fpt.blog.models.category.response.CategoryResponse;
import com.fpt.blog.models.comment.response.CommentResponse;
import com.fpt.blog.models.post.request.CommentPostRequest;
import com.fpt.blog.models.post.request.CreatePostRequest;
import com.fpt.blog.models.post.request.GetAllPostRequest;
import com.fpt.blog.models.post.request.UpdatePostRequest;
import com.fpt.blog.models.post.response.PostResponse;
import com.fpt.blog.models.reaction.response.ReactionResponse;
import com.fpt.blog.models.tag.TagResponse;
import com.fpt.blog.models.user.response.UserResponse;
import com.fpt.blog.services.CategoryService;
import com.fpt.blog.services.PostService;
import com.fpt.blog.services.TagService;
import com.fpt.blog.services.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/posts")
@Slf4j
public class PostController {

    private final CategoryService categoryService;

    private final UserService userService;

    private final PostService postService;

    private final TagService tagService;


    @GetMapping
    public String allPosts(@ModelAttribute GetAllPostRequest request, Model model) {

        request.setStatus(PostStatus.APPROVED);

        model.addAttribute("posts", postService.getAllPosts(request));
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("tags", tagService.getAllTags());
        model.addAttribute("search", request.getSearch());
        model.addAttribute("tag", request.getTag());

        if (request.getCategoryId() != null) {
            CategoryResponse category = categoryService.getCategory(request.getCategoryId());
            if (category != null) {
                model.addAttribute("category", category.getName());
            }
        }

        return "posts";

    }


    @GetMapping("create")
    public String createPostForm(Model model) {
        List<CategoryResponse> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "create-post";
    }

    @PostMapping("create")
    public String createPost(@ModelAttribute CreatePostRequest request, Model model, HttpSession session) {
        model.addAttribute("title", request.getTitle());
        model.addAttribute("categoryId", request.getCategoryId());
        model.addAttribute("tags", request.getTags());
        model.addAttribute("content", request.getContent());
        model.addAttribute("description", request.getDescription());
        model.addAttribute("commentEnabled", request.isCommentEnabled());
        // validate
        try {
            if (userService.getLoginUser() == null) {
                return "redirect:/auth/login";
            }

            // create post
            if (categoryService.getCategory(request.getCategoryId()) == null) {
                session.setAttribute("error", "Category not found");
            }

            Post savedPost = postService.createPost(request);
            if (savedPost != null) {
                session.setAttribute("message", "Create post successfully!");
                return "redirect:/posts/" + savedPost.getId();
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }

        session.setAttribute("error", "Internal server error");
        return "create-post";
    }


    @GetMapping("{id}")
    public String getPostDetail(@PathVariable("id") long postId, Model model) {
        PostResponse post = postService.getPost(postId);
        if (post == null) {
            return "not-found";
        }

        model.addAttribute("post", post);

        // reactions
        UserResponse loggedUser = userService.getLoginUser();
        if (loggedUser != null) {
            boolean liked = post.getReactions()
                    .stream()
                    .anyMatch(reaction -> reaction.getUser().getId() == loggedUser.getId() && ReactType.LIKE.equals(reaction.getType()));

            boolean disliked = post.getReactions()
                    .stream()
                    .anyMatch(reaction -> reaction.getUser().getId() == loggedUser.getId() && ReactType.DISLIKE.equals(reaction.getType()));

            boolean loved = post.getReactions()
                    .stream()
                    .anyMatch(reaction -> reaction.getUser().getId() == loggedUser.getId() && ReactType.LOVE.equals(reaction.getType()));

            model.addAttribute("liked", liked);
            model.addAttribute("disliked", disliked);
            model.addAttribute("loved", loved);
        }

        // count
        long likeCount = post.getReactions().stream().filter(reaction -> ReactType.LIKE.equals(reaction.getType())).count();
        long dislikeCount = post.getReactions().stream().filter(reaction -> ReactType.DISLIKE.equals(reaction.getType())).count();
        long loveCount = post.getReactions().stream().filter(reaction -> ReactType.LOVE.equals(reaction.getType())).count();

        model.addAttribute("likeCount", likeCount);
        model.addAttribute("dislikeCount", dislikeCount);
        model.addAttribute("loveCount", loveCount);

        // Comments
        model.addAttribute("comments", postService.getAllComments(post.getId()));

        return "post-detail";
    }


    @PostMapping("{postId}/delete")
    public String deletePost(@PathVariable("postId") long postId, HttpSession session) {
        try {
            PostResponse post = postService.getPost(postId);
            if (post == null) {
                return "not-found";
            }

            postService.deletePost(postId);
            session.setAttribute("message", "Delete post successfully!");

            return "redirect:/home";
        } catch (Exception ex) {
            session.setAttribute("error", "Internal server error");
            return "redirect:/home";
        }
    }

    @GetMapping("{postId}/update")
    public String updatePost(@PathVariable("postId") long postId, Model model, HttpSession session) {
        PostResponse post = postService.getPost(postId);
        if (post == null) {
            return "not-found";
        }

        model.addAttribute("post", post);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("tagValue", String.join(", " ,post.getTags().stream().map(TagResponse::getName).toList()));
        return "update-post";
    }

    @PostMapping("{postId}/update")
    public String updatePost(@PathVariable("postId") long postId, @ModelAttribute UpdatePostRequest request, HttpSession session) {
        try {
            PostResponse post = postService.updatePost(postId, request);
            session.setAttribute("message", "Update post successfully");
            return "redirect:/posts/" + post.getId();

        } catch (Exception ex) {
            session.setAttribute("error", ex.getMessage());
        }
        return "redirect:/posts/" + request.getPostId();
    }


    @GetMapping("my-posts")
    public String getAllMyPost(Model model) {
        UserResponse user = userService.getLoginUser();
        if (user == null) {
            return "redirect:/auth/login";
        }

        List<PostResponse> posts = postService.getAllPosts(new GetAllPostRequest()
                .setUserId(user.getId()));

        model.addAttribute("posts", posts);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("tags", tagService.getAllTags());

        return "my-posts";
    }

    @PostMapping("{postId}/reactions")
    public String react(@PathVariable("postId") long id,  @RequestParam ReactType type, HttpSession session) {
        try {
            PostResponse post = postService.getPost(id);
            if (post == null) {
                return "not-found";
            }
            ReactionResponse react = postService.react(id, type);
        } catch (Exception ex) {
            session.setAttribute("error", ex.getMessage());
        }

        return "redirect:/posts/" + id;

    }

    @PostMapping("{postId}/remove-reactions")
    public String removeReaction(@PathVariable("postId") long id,  @RequestParam ReactType type, HttpSession session) {
        try {
            PostResponse post = postService.getPost(id);
            if (post == null) {
                return "not-found";
            }
            ReactionResponse react = postService.removeReaction(id, type);
        } catch (Exception ex) {
            session.setAttribute("error", ex.getMessage());
        }

        return "redirect:/posts/" + id;
    }

    @PostMapping("{postId}/comments")
    public String commentPost(@PathVariable("postId") long id, @ModelAttribute CommentPostRequest request, HttpSession session) {
        try {
            PostResponse post = postService.getPost(id);
            if (post == null) {
                return "not-found";
            }
            CommentResponse react = postService.commentPost(id, request);
            session.setAttribute("message", "Comment successfully");
        } catch (Exception ex) {
            session.setAttribute("error", ex.getMessage());
            log.error(ex.getMessage());
        }

        return "redirect:/posts/" + id;
    }

    @PostMapping("{postId}/delete-comment/{commentId}")
    public String deleteComment(@PathVariable("postId") long postId, @PathVariable("commentId") long commentId, HttpSession session) {
        try {
            PostResponse post = postService.getPost(postId);
            if (post == null) {
                return "not-found";
            }

            CommentResponse comment = postService.deleteComment(commentId);
            session.setAttribute("message", "Delete Comment successfully");
        } catch (Exception ex) {
            session.setAttribute("error", ex.getMessage());
            log.error(ex.getMessage());
        }

        return "redirect:/posts/" + postId;
    }


}

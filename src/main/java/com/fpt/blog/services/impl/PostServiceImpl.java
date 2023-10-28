package com.fpt.blog.services.impl;

import com.fpt.blog.entities.*;
import com.fpt.blog.enums.MemberType;
import com.fpt.blog.enums.PostStatus;
import com.fpt.blog.enums.ReactType;
import com.fpt.blog.enums.Role;
import com.fpt.blog.mappings.CommentMapper;
import com.fpt.blog.mappings.PostMapper;
import com.fpt.blog.mappings.ReactionMapper;
import com.fpt.blog.models.comment.request.DeleteCommentRequest;
import com.fpt.blog.models.comment.response.CommentResponse;
import com.fpt.blog.models.common.request.BaseFilterRequest;
import com.fpt.blog.models.post.request.*;
import com.fpt.blog.models.post.response.PostResponse;
import com.fpt.blog.models.reaction.response.ReactionResponse;
import com.fpt.blog.repositories.*;
import com.fpt.blog.services.FileService;
import com.fpt.blog.services.ISendGmailService;
import com.fpt.blog.services.PostService;
import com.fpt.blog.utils.UploadMedia;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final UserRepository userRepository;

    private final CategoryRepository categoryRepository;

    private final TagRepository tagRepository;

    private final PostRepository postRepository;

    private final PostMapper postMapper;

    private final AwardRepository awardRepository;

    private final PostAwardRepository postAwardRepository;

    private final ReactionMapper reactionMapper;

    private final ReactionRepository reactionRepository;

    private final CommentRepository commentRepository;

    private final CommentMapper commentMapper;

    private final FileService fileService;

    private final ISendGmailService sendGmailService;

    @Override
    @Transactional
    @SneakyThrows
    public Post createPost(CreatePostRequest request) {
        Category category = categoryRepository
                .findById(request.getCategoryId())
                .orElseThrow(() -> new Exception("Category not found"));

        User author = userRepository.findLoginUser()
                .orElseThrow(() -> new Exception("Unauthorized"));


        Post post = new Post()
                .setTitle(request.getTitle())
                .setContent(request.getContent())
                .setCategory(category)
                .setUser(author)
                .setDescription(request.getDescription())
                .setCommentEnabled(request.isCommentEnabled())
                .setStatus(Role.MENTOR.equals(author.getRole()) || Role.ADMIN.equals(author.getRole()) ? PostStatus.APPROVED : PostStatus.WAITING);

        // upload thumbnail
        if (!request.getThumbnail().isEmpty()) {
            UploadMedia uploadMedia = fileService.uploadFile(request.getThumbnail());
            post.setThumbnail(uploadMedia.getUrl());
        }

        if (request.getTags() != null) {
            Set<Tag> tags = new HashSet<>();
            for (String t : request.getTags().split(",")) {
                t = t.trim().toLowerCase();
                Tag tag = tagRepository
                        .findFirstByName(t)
                        .orElse(new Tag(t));

                tag.getPosts().add(post);
                tags.add(tag);
            }
            post.setTags(tags);
        }

        Post savedPost = postRepository.save(post);

        if (PostStatus.APPROVED.equals(savedPost.getStatus())) {
            updateMemberType(post.getUser());
        }

        return savedPost;
    }

    @Override
    public Page<PostResponse> getAllPosts(BaseFilterRequest<Post> request) {
        return postRepository.findAll(request.getSpecification(), request.getPageable())
                .map(postMapper::toResponse);
    }

    @Override
    @SneakyThrows
    @Transactional
    public PostResponse approvePost(long id, ApprovePostRequest request) {
        Post post = postRepository.findById(id).orElseThrow(() -> new Exception("Post not found"));
        User reviewer = userRepository.findLoginUser()
                .orElseThrow(() -> new Exception("Unauthorized"));

        Set<PostAward> awards = new HashSet<>();
        for (long awardId : request.getAwardIds()) {
            Award award = awardRepository.findById(awardId)
                    .orElseThrow(() -> new Exception("Award not found"));

            awards.add(new PostAward()
                    .setPost(post)
                    .setAward(award)
                    .setCreatedBy(reviewer));
        }

        postAwardRepository.saveAll(awards);

        post.setApprovedAt(LocalDateTime.now());
        post.setReviewBy(reviewer);
        post.setStatus(PostStatus.APPROVED);

        Post savedPost = postRepository.save(post);

        updateMemberType(post.getUser());

        return postMapper.toResponse(savedPost);
    }

    @Override
    @SneakyThrows
    @Transactional
    public PostResponse rejectPost(long id, RejectPostRequest request) {
        Post post = postRepository.findById(id).orElseThrow(() -> new Exception("Post not found"));
        User reviewer = userRepository.findLoginUser()
                .orElseThrow(() -> new Exception("Unauthorized"));

        post.setRejectedAt(LocalDateTime.now());
        post.setRejectReason(request.getReason());
        post.setReviewBy(reviewer);
        post.setStatus(PostStatus.REJECTED);

        Post savedPost = postRepository.save(post);

        return postMapper.toResponse(savedPost);
    }

    @Override
    @SneakyThrows
    @Transactional
    public PostResponse getPost(long id) {
        Post post = postRepository.findById(id).orElse(null);

        if (post != null && PostStatus.APPROVED.equals(post.getStatus())) {
            post.setViewCount(post.getViewCount() + 1);
            post = postRepository.save(post);
        }
        return postMapper.toResponse(post);
    }

    @Override
    @SneakyThrows
    @Transactional
    public PostResponse deletePost(long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new Exception("Post not found"));

        postRepository.delete(post);

        return postMapper.toResponse(post);
    }

    @Override
    @SneakyThrows
    @Transactional
    public PostResponse updatePost(long postId, UpdatePostRequest request) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new Exception("Post not found"));
        User author = userRepository.findLoginUser()
                .orElseThrow(() -> new Exception("Unauthorized"));

        Category category = categoryRepository
                .findById(request.getCategoryId())
                .orElseThrow(() -> new Exception("Category not found"));


        post.setTitle(request.getTitle());
        post.setCommentEnabled(request.isCommentEnabled());
        post.setContent(request.getContent());
        post.setDescription(request.getDescription());
        post.setStatus(Role.MENTOR.equals(author.getRole()) || Role.ADMIN.equals(author.getRole()) ? PostStatus.APPROVED : PostStatus.WAITING);
        post.setCategory(category);
        post.getTags().clear();

        // upload thumbnail
        if (!request.getThumbnail().isEmpty()) {
            UploadMedia uploadMedia = fileService.uploadFile(request.getThumbnail());
            post.setThumbnail(uploadMedia.getUrl());
        }

        if (request.getTags() != null) {
            Set<Tag> tags = new HashSet<>();
            for (String t : request.getTags().split(",")) {
                t = t.trim().toLowerCase();
                Tag tag = tagRepository
                        .findFirstByName(t)
                        .orElse(new Tag(t));

                tag.getPosts().add(post);
                tags.add(tag);
            }
            post.setTags(tags);
        }

        post = postRepository.save(post);

        return postMapper.toResponse(post);
    }

    @Override
    @SneakyThrows
    @Transactional
    public ReactionResponse react(long id, ReactType type) {
        Post post = postRepository.findById(id).orElseThrow(() -> new Exception("Post not found"));

        User reactor = userRepository.findLoginUser()
                .orElseThrow(() -> new Exception("Unauthorized"));

        // Check exist reaction
        Reaction reaction = reactionRepository.findFirstByPostIdAndUserIdAndType(post.getId(), reactor.getId(), type)
                .orElse(null);

        if (reaction != null) {
            log.info("Exist reaction");
            return reactionMapper.toResponse(reaction);
        }

        reaction = new Reaction()
                .setType(type)
                .setPost(post)
                .setUser(reactor);

        // remove existed reactions
        List<Reaction> reactions = reactionRepository.findByPostIdAndUserId(post.getId(), reactor.getId());
        if (reactions != null) {
            reactions.forEach(post.getReactions()::remove);
        }

        reaction = reactionRepository.save(reaction);
        postRepository.save(post);

        return reactionMapper.toResponse(reaction);
    }

    @Override
    @SneakyThrows
    @Transactional
    public ReactionResponse removeReaction(long id, ReactType type) {
        Post post = postRepository.findById(id).orElseThrow(() -> new Exception("Post not found"));

        User reactor = userRepository.findLoginUser()
                .orElseThrow(() -> new Exception("Unauthorized"));

        Reaction reaction = reactionRepository.findFirstByPostIdAndUserIdAndType(post.getId(), reactor.getId(), type)
                .orElse(null);

        if (reaction != null) {
            post.getReactions().remove(reaction);
            postRepository.save(post);
        }

        return reactionMapper.toResponse(reaction);
    }

    @Override
    public List<CommentResponse> getAllComments(long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);

        comments.sort((c1, c2) -> c1.getCreatedAt().isBefore(c2.getCreatedAt()) ? 1 : -1);

        return comments.stream().map(commentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @SneakyThrows
    @Transactional
    public CommentResponse commentPost(long id, CommentPostRequest request, HttpSession session) {
        Post post = postRepository.findById(id).orElseThrow(() -> new Exception("Post not found"));

        User reactor = userRepository.findLoginUser()
                .orElseThrow(() -> new Exception("Unauthorized"));

        if (reactor.getDelCmtNumber() != null){
            if(reactor.getDelCmtNumber() >= 3){
                if (reactor.getCmtBanExpiredAt().isAfter(LocalDateTime.now())){
                    return null;
                }
            }

        }


        Comment comment = new Comment()
                .setPost(post)
                .setUser(reactor)
                .setContent(request.getContent());

        if (request.getParentCommentId() != null) {
            Comment parentComment = commentRepository.findById(request.getParentCommentId())
                    .orElseThrow(() -> new Exception("Parent comment not found"));

            comment.setParent(parentComment);
        }

        Comment saved = commentRepository.save(comment);

        return commentMapper.toResponse(saved);

    }

    @Override
    @SneakyThrows
    @Transactional
    public CommentResponse deleteComment(long id, Role role, DeleteCommentRequest request) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new Exception("Comment not found"));
        User user = comment.getUser();
        if(role.equals(Role.ADMIN) || role.equals(Role.MENTOR)){
            Integer cmtDelNumer = user.getDelCmtNumber();
            if (cmtDelNumer == null){
                cmtDelNumer = 1;
            }
            else {
                cmtDelNumer++;
                if (cmtDelNumer == 3){
                    user.setCmtBanAt( LocalDateTime.now());
                    user.setCmtBanExpiredAt(LocalDateTime.now().plus(7, ChronoUnit.DAYS));
                }else if(cmtDelNumer >= 3 && user.getCmtBanExpiredAt() != null){
                    if(user.getCmtBanExpiredAt().isBefore(LocalDateTime.now())){
                        cmtDelNumer = 1;
                    }
                }

            }
            comment.getUser().setDelCmtNumber(cmtDelNumer);

            ExecutorService executor = Executors.newSingleThreadExecutor();
            // Gửi công việc gửi email đến ExecutorService
            executor.submit(() -> {
                sendGmailService.sendMailBanCommentV1(user,comment,request);
            });

            // Đóng ExecutorService sau khi đã sử dụng
            executor.shutdown();

            userRepository.save(user);
        }

        commentRepository.delete(comment);
        return commentMapper.toResponse(comment);
    }

    @Override
    public List<PostResponse> getFeaturedPosts(int top) {
        return postRepository
                .findFeaturedPost(PostStatus.APPROVED, PageRequest.of(0, top))
                .stream()
                .map(postMapper::toResponse)
                .toList();
    }

    private void updateMemberType(User user) {
        long postCount = postRepository.countByUserId(user.getId());

        if (postCount >= MemberType.COPPER.getPostCount()) {
            user.setMemberType(MemberType.COPPER);
        }

        if (postCount >= MemberType.SILVER.getPostCount()) {
            user.setMemberType(MemberType.SILVER);
        }

        if (postCount >= MemberType.GOLD.getPostCount()) {
            user.setMemberType(MemberType.SILVER);
        }

        if (postCount >= MemberType.DIAMOND.getPostCount()) {
            user.setMemberType(MemberType.DIAMOND);
        }

        userRepository.save(user);
    }
}

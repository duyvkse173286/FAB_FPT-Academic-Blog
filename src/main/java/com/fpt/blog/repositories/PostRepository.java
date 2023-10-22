package com.fpt.blog.repositories;

import com.fpt.blog.entities.Post;
import com.fpt.blog.enums.PostStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {

    @Query("SELECT post FROM Post post WHERE post.status = ?1 ORDER BY post.viewCount DESC")
    List<Post> findFeaturedPost(PostStatus status, Pageable pageable);

    long countByUserId(long userId);

}

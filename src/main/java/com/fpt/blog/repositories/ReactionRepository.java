package com.fpt.blog.repositories;

import com.fpt.blog.entities.Comment;
import com.fpt.blog.entities.Reaction;
import com.fpt.blog.enums.ReactType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long>, JpaSpecificationExecutor<Reaction> {

    Optional<Reaction> findFirstByPostIdAndUserIdAndType(long postId, long userId, ReactType type);

    List<Reaction> findByPostIdAndUserId(long postId, long userId);

}

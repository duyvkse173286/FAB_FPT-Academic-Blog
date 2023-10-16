package com.fpt.blog.repositories;

import com.fpt.blog.entities.PostAward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PostAwardRepository extends JpaRepository<PostAward, Long>, JpaSpecificationExecutor<PostAward> {
}

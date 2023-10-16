package com.fpt.blog.repositories;

import com.fpt.blog.entities.Award;
import com.fpt.blog.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AwardRepository extends JpaRepository<Award, Long>, JpaSpecificationExecutor<Award> {
}

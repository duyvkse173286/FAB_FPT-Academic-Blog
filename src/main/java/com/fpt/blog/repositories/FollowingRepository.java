package com.fpt.blog.repositories;

import com.fpt.blog.entities.Following;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowingRepository extends JpaRepository<Following, Long> {

    Optional<Following> findFirstByUserIdAndFollowerId(long userId, long followerId);

    List<Following> findByUserId(long following);

    List<Following> findByFollowerId(long followerId);

}

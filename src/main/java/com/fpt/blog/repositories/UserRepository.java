package com.fpt.blog.repositories;

import com.fpt.blog.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> findByEmail(String email);


    @Query("SELECT u FROM User u WHERE u.email = ?#{ principal.username}")
    Optional<User> findLoginUser();

}

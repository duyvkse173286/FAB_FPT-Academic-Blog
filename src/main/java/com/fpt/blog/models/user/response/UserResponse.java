package com.fpt.blog.models.user.response;

import com.fpt.blog.enums.MemberType;
import com.fpt.blog.enums.Role;
import com.fpt.blog.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private long id;

    private String email;

    private String avatar;

    private String password;

    private String phoneNumber;

    private String name;

    private LocalDate dob;

    private UserStatus status;

    private MemberType memberType;

    private LocalDateTime bannedAt;

    private LocalDateTime inactiveAt;

    private String description;

    private Role role;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    private boolean isDeleted;

}

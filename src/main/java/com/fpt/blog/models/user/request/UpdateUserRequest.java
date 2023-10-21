package com.fpt.blog.models.user.request;

import com.fpt.blog.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {
    private Role role;

    private String password;

    private String confirmPassword;

    private String phoneNumber;

    private String name;

    private LocalDate dob;
}

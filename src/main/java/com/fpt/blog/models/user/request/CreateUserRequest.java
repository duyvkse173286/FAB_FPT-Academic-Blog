package com.fpt.blog.models.user.request;

import com.fpt.blog.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CreateUserRequest {

    private Role role;

    private String email;

    private String password;

    private String confirmPassword;

    private String phoneNumber;

    private String name;

    private LocalDate dob;

}

package com.fpt.blog.models.user.request;

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

    private String email;

    private String password;

    private String confirmPassword;

    private String phoneNumber;

    private String name;

    private LocalDate dob;

}

package com.fpt.blog.models.user.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProfileRequest {

    private MultipartFile avatar;

    private String name;

    private LocalDate dob;

    private String description;

    private String phoneNumber;
    

}

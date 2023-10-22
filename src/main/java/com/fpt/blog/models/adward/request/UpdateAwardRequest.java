package com.fpt.blog.models.adward.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAwardRequest {

    private String name;

    private String description;

    private MultipartFile image;

}

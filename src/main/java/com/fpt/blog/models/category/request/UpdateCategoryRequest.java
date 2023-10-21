package com.fpt.blog.models.category.request;

import com.fpt.blog.enums.Collection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UpdateCategoryRequest {
    private String name;

    private String description;

    private Collection collection;

    private MultipartFile image;
}

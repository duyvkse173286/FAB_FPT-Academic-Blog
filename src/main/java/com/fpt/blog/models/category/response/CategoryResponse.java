package com.fpt.blog.models.category.response;

import com.fpt.blog.enums.Collection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {

    private long id;

    private String name;

    private String image;

    private String description;

    private Collection collection;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}

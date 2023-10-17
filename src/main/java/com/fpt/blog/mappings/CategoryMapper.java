package com.fpt.blog.mappings;

import com.fpt.blog.entities.Category;
import com.fpt.blog.models.category.response.CategoryResponse;
import org.mapstruct.Mapper;

@Mapper
public interface CategoryMapper {

    CategoryResponse toResponse(Category category);

}

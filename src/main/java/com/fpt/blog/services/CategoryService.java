package com.fpt.blog.services;

import com.fpt.blog.models.category.request.GetAllCategoryRequest;
import com.fpt.blog.models.category.response.CategoryResponse;

import java.util.List;

public interface CategoryService {

    List<CategoryResponse> getAllCategories(GetAllCategoryRequest request);

    List<CategoryResponse> getAllCategories();

    CategoryResponse getCategory(long id);

}


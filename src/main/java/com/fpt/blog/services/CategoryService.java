package com.fpt.blog.services;

import com.amazonaws.services.kms.model.CreateAliasRequest;
import com.fpt.blog.models.category.request.CreateCategoryRequest;
import com.fpt.blog.models.category.request.GetAllCategoryRequest;
import com.fpt.blog.models.category.request.UpdateCategoryRequest;
import com.fpt.blog.models.category.response.CategoryResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CategoryService {

    List<CategoryResponse> getAllCategories(GetAllCategoryRequest request);

    Page<CategoryResponse> getAllCategoriesFilterPaging(GetAllCategoryRequest request);

    List<CategoryResponse> getAllCategories();

    CategoryResponse getCategory(long id);

    CategoryResponse createCategory(CreateCategoryRequest request);

    CategoryResponse updateCategory(long id, UpdateCategoryRequest request);

    CategoryResponse removeCategory(long id);

}


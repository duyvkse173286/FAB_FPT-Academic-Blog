package com.fpt.blog.services.impl;

import com.fpt.blog.entities.Category;
import com.fpt.blog.mappings.CategoryMapper;
import com.fpt.blog.models.category.request.GetAllCategoryRequest;
import com.fpt.blog.models.category.response.CategoryResponse;
import com.fpt.blog.repositories.CategoryRepository;
import com.fpt.blog.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryResponse> getAllCategories(GetAllCategoryRequest request) {
        return categoryRepository
                .findAll(request.getSpecification()).stream()
                .map(categoryMapper::toResponse).toList();
    }
    @Override
    public List<CategoryResponse> getAllCategories() {
        return getAllCategories(new GetAllCategoryRequest());
    }

    @Override
    public CategoryResponse getCategory(long id) {
        Category cate = categoryRepository
                .findById(id)
                .orElse(null);

        return categoryMapper.toResponse(cate);
    }
}

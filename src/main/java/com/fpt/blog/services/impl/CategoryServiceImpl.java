package com.fpt.blog.services.impl;

import com.fpt.blog.entities.Category;
import com.fpt.blog.mappings.CategoryMapper;
import com.fpt.blog.models.category.request.CreateCategoryRequest;
import com.fpt.blog.models.category.request.GetAllCategoryRequest;
import com.fpt.blog.models.category.request.UpdateCategoryRequest;
import com.fpt.blog.models.category.response.CategoryResponse;
import com.fpt.blog.repositories.CategoryRepository;
import com.fpt.blog.services.CategoryService;
import com.fpt.blog.services.FileService;
import com.fpt.blog.utils.UploadMedia;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    private final FileService fileService;

    @Override
    public List<CategoryResponse> getAllCategories(GetAllCategoryRequest request) {
        return categoryRepository
                .findAll(request != null
                        ? request.getSpecification()
                        : null).stream()
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

    @Override
    @SneakyThrows
    @Transactional
    public CategoryResponse createCategory(CreateCategoryRequest request) {

        Category category = new Category()
                .setName(request.getName())
                .setDescription(request.getDescription())
                .setCollection(request.getCollection());

        if (!request.getImage().isEmpty()) {
            UploadMedia uploadMedia = fileService.uploadFile(request.getImage());
            category.setImage(uploadMedia.getUrl());
        }

        Category savedCate = categoryRepository.save(category);

        return categoryMapper.toResponse(savedCate);
    }

    @Override
    @SneakyThrows
    @Transactional
    public CategoryResponse updateCategory(long id, UpdateCategoryRequest request) {
        Category cate = categoryRepository
                .findById(id)
                .orElseThrow(() -> new Exception("Category not found"));

        cate.setName(request.getName())
                .setDescription(request.getDescription())
                .setCollection(request.getCollection());

        if (!request.getImage().isEmpty()) {
            UploadMedia uploadMedia = fileService.uploadFile(request.getImage());
            cate.setImage(uploadMedia.getUrl());
        }

        Category savedCate = categoryRepository.save(cate);

        return categoryMapper.toResponse(savedCate);
    }

    @Override
    @SneakyThrows
    @Transactional
    public CategoryResponse removeCategory(long id) {
        Category cate = categoryRepository
                .findById(id)
                .orElseThrow(() -> new Exception("Category not found"));

        categoryRepository.delete(cate);

        return categoryMapper.toResponse(cate);

    }
}

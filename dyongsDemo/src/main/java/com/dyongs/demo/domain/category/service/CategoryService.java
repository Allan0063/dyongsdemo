package com.dyongs.demo.domain.category.service;

import com.dyongs.demo.domain.category.dto.CategoryRequest;
import com.dyongs.demo.domain.category.dto.CategoryResponse;
import com.dyongs.demo.domain.category.entity.Category;
import com.dyongs.demo.domain.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("이미 존재하는 카테고리입니다. name=" + request.getName());
        }

        Category category = Category.builder()
                .name(request.getName())
                .build();

        Category saved = categoryRepository.save(category);
        return new CategoryResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> getCategories() {
        return categoryRepository.findAll().stream()
                .map(CategoryResponse::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public Category getCategoryEntity(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다. id=" + id));
    }
}

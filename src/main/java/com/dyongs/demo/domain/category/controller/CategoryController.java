package com.dyongs.demo.domain.category.controller;

import com.dyongs.demo.domain.category.dto.CategoryListResponse;
import com.dyongs.demo.domain.category.dto.CategoryRequest;
import com.dyongs.demo.domain.category.dto.CategoryResponse;
import com.dyongs.demo.domain.category.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public CategoryResponse createCategory(@Valid @RequestBody CategoryRequest request) {
        return categoryService.createCategory(request);
    }

    @GetMapping
    public List<CategoryResponse> getCategories() {
        return categoryService.getCategories();
    }

    @GetMapping("/page")
    public Page<CategoryListResponse> getCategoryList(
            @RequestParam(required = false) String keyword,
            Pageable pageable
    ) {
        return categoryService.getCategoryList(keyword, pageable);
    }
}

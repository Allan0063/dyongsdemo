package com.dyongs.demo.domain.category.service;

import com.dyongs.demo.domain.category.dto.CategoryListResponse;
import com.dyongs.demo.domain.category.dto.CategoryRequest;
import com.dyongs.demo.domain.category.dto.CategoryResponse;
import com.dyongs.demo.domain.category.entity.Category;
import com.dyongs.demo.domain.category.exception.InvalidSortException;
import com.dyongs.demo.domain.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

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


    // 목록(OneToMany, ManyToMany)
    @Transactional(readOnly = true)
    public Page<CategoryListResponse> getCategoryList(String keyword, Pageable pageable) {
        Pageable safePageable = validateSort(pageable);
        return categoryRepository.searchCategoryList(keyword, safePageable);
    }

    private Pageable validateSort(Pageable pageable) {
        Set<String> allowed = Set.of("id", "name"); // Category 엔티티 필드명

        Sort sort = pageable.getSort();
        if (sort.isUnsorted()) {
            // 기본 정렬 정책(원하면 바꿔도 됨)
            return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                    Sort.by(Sort.Order.desc("id")));
        }

        for (Sort.Order order : sort) {
            if (!allowed.contains(order.getProperty())) {
                throw new InvalidSortException("허용되지 않은 정렬 필드: " + order.getProperty());
            }
        }
        return pageable;
    }
}

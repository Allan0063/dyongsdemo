package com.dyongs.demo.domain.category.service;

import com.dyongs.demo.domain.category.CategorySortField;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int MAX_PAGE_SIZE = 50;

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
        Pageable safe = validateAndNormalizePageable(pageable);
        return categoryRepository.searchCategoryList(keyword, safe);
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

    private Pageable validateAndNormalizePageable(Pageable pageable) {
        // ✅ page/size 방어 (선택인데 실무에선 거의 함)
        int page = Math.max(pageable.getPageNumber(), 0);

        int size = pageable.getPageSize();
        if (size <= 0) size = DEFAULT_PAGE_SIZE;
        if (size > MAX_PAGE_SIZE) size = MAX_PAGE_SIZE;

        Sort normalizedSort = normalizeSort(pageable.getSort());
        return PageRequest.of(page, size, normalizedSort);
    }

    private Sort normalizeSort(Sort sort) {
        // ✅ 정렬이 없으면 기본 정렬 정책
        if (sort == null || sort.isUnsorted()) {
            return Sort.by(Sort.Order.desc("id"));
        }

        List<Sort.Order> orders = new ArrayList<>();

        for (Sort.Order order : sort) {
            String requestedKey = order.getProperty(); // 사용자가 준 sort 키
            CategorySortField field = CategorySortField.fromKey(requestedKey);

            if (field == null) {
                throw new InvalidSortException("허용되지 않은 정렬 필드: " + requestedKey);
            }

            // ✅ 요청 키 → 실제 엔티티 필드명으로 매핑
            Sort.Direction direction = order.getDirection(); // Spring이 asc/desc 파싱
            Sort.Order mapped = new Sort.Order(direction, field.getProperty());

            // (선택) 대소문자 무시 정렬 같은 옵션
            if (order.isIgnoreCase()) {
                mapped = mapped.ignoreCase();
            }

            orders.add(mapped);
        }

        return Sort.by(orders);
    }
}

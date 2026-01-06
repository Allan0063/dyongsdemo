package com.dyongs.demo.domain.category.dto;

import lombok.Getter;

@Getter
public class CategoryListResponse {
    private final Long id;
    private final String name;

    public CategoryListResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}

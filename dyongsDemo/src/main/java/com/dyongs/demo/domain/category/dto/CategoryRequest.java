package com.dyongs.demo.domain.category.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategoryRequest {
    @NotBlank(message = "카테고리명은 필수입니다.")
    private String name;
}

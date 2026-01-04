package com.dyongs.demo.domain.product.dto;

import com.dyongs.demo.domain.product.entity.Product;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class ProductResponse {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private int stock;
    private Long categoryId;
    private String categoryName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ProductResponse(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.stock = product.getStock();

        this.categoryId = product.getCategory() != null ? product.getCategory().getId() : null;
        this.categoryName = product.getCategory() != null ? product.getCategory().getName() : null;

        this.createdAt = product.getCreatedAt();
        this.updatedAt = product.getUpdatedAt();
    }
}

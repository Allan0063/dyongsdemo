package com.dyongs.demo.domain.product.controller;

import com.dyongs.demo.domain.product.dto.ProductRequest;
import com.dyongs.demo.domain.product.dto.ProductResponse;
import com.dyongs.demo.domain.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    // Create
    @PostMapping
    public ProductResponse create(@Valid @RequestBody ProductRequest request) {
        return productService.createProduct(request);
    }

    // Read - 단일
    @GetMapping("/{id}")
    public ProductResponse getOne(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    // Read - 전체
    @GetMapping
    public List<ProductResponse> getAll() {
        return productService.getProducts();
    }

    // Update
    @PutMapping("/{id}")
    public ProductResponse update(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request
    ) {
        return productService.updateProduct(id, request);
    }

    // Delete
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}

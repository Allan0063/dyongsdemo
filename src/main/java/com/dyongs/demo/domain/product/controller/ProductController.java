package com.dyongs.demo.domain.product.controller;

import com.dyongs.demo.domain.product.dto.ProductRequest;
import com.dyongs.demo.domain.product.dto.ProductResponse;
import com.dyongs.demo.domain.product.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    // 조회 API들은 필요에 따라 userId 안 써도 됨
    // Read - 단일
    @GetMapping("/{id}")
    public ProductResponse getOne(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    // 목록 + 검색 + 페이징 + 정렬
    @GetMapping
    public Page<ProductResponse> getProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,     // 검색어 (옵션)
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String sort
    ) {
        return productService.getProducts(keyword, categoryId, page, size, sort);
    }

    // 검색
    @GetMapping("/search")
    public Page<ProductResponse> searchProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String sort
    ) {
        return productService.searchProducts(keyword, page, size, sort);
    }

    // 상품 생성 - 로그인 필요 (JWT 필터 통과한 상태)
    @PostMapping
    public ProductResponse createProduct(
            @Valid @RequestBody ProductRequest request,
            HttpServletRequest httpRequest
    ) {
        Long userId = (Long) httpRequest.getAttribute("userId");
        return productService.createProduct(request, userId);
    }

    // Update
    @PutMapping("/{id}")
    public ProductResponse updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request,
            HttpServletRequest httpRequest
    ) {
        Long userId = (Long) httpRequest.getAttribute("userId");
        return productService.updateProduct(id, request, userId);
    }

    // Delete
    @DeleteMapping("/{id}")
    public void deleteProduct(
            @PathVariable Long id,
            HttpServletRequest httpRequest
    ) {
        Long userId = (Long) httpRequest.getAttribute("userId");
        productService.deleteProduct(id, userId);
    }
}

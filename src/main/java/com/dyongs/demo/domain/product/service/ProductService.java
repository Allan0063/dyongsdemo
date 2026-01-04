package com.dyongs.demo.domain.product.service;

import com.dyongs.demo.domain.category.entity.Category;
import com.dyongs.demo.domain.category.exception.CategoryNotFoundException;
import com.dyongs.demo.domain.category.repository.CategoryRepository;
import com.dyongs.demo.domain.product.dto.ProductRequest;
import com.dyongs.demo.domain.product.dto.ProductResponse;
import com.dyongs.demo.domain.product.entity.Product;
import com.dyongs.demo.domain.product.exception.ProductAccessDeniedException;
import com.dyongs.demo.domain.product.exception.ProductNotFoundException;
import com.dyongs.demo.domain.product.repository.ProductRepository;
import com.dyongs.demo.domain.user.entity.User;
import com.dyongs.demo.domain.user.exception.UserNotFoundException;
import com.dyongs.demo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    // Read - 단일
    @Transactional(readOnly = true)
    public ProductResponse getProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product Not Found"));
        return new ProductResponse(product);
    }

    // 페이징
    @Transactional(readOnly = true)
    public Page<ProductResponse> getProducts(String keyword, Long categoryId, int page, int size, String sort) {

        // sort 파라미터 파싱 (예: "id,desc" or "price,asc")
        String[] sortParams = sort.split(",");
        String sortProperty = sortParams[0];
        Sort.Direction direction = Sort.Direction.DESC;

        if (sortParams.length > 1) {
            direction = Sort.Direction.fromString(sortParams[1]);
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortProperty));

        Page<Product> productPage;

        boolean hasKeyword = (keyword != null && !keyword.isBlank());
        boolean hasCategory = (categoryId != null);

        if (!hasKeyword && !hasCategory) {
            productPage = productRepository.findAll(pageable);
        } else if (hasKeyword && !hasCategory) {
            productPage = productRepository.findByNameContainingIgnoreCase(keyword, pageable);
        } else if (!hasKeyword && hasCategory) {
            productPage = productRepository.findByCategory_Id(categoryId, pageable);
        } else {
            productPage = productRepository.findByCategory_IdAndNameContainingIgnoreCase(categoryId, keyword, pageable);
        }

        return productPage.map(ProductResponse::new);
    }

    // 검색
    public Page<ProductResponse> searchProducts(String keyword, int page, int size, String sort) {
        String[] sortParams = sort.split(",");
        String sortProperty = sortParams[0];
        Sort.Direction direction = sortParams.length > 1
                ? Sort.Direction.fromString(sortParams[1])
                : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortProperty));

        Page<Product> productPage;

        if (keyword == null || keyword.isBlank()) {
            productPage = productRepository.findAll(pageable);
        } else {
            productPage = productRepository.findByNameContainingIgnoreCase(keyword, pageable);
        }

        return productPage.map(ProductResponse::new);
    }

    // Create
    public ProductResponse createProduct(ProductRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException(request.getCategoryId()));

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .stock(request.getStock())
                .category(category)
                .createdBy(user)
                .build();

        Product saved = productRepository.save(product);
        return new ProductResponse(saved);
    }

    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request, Long userId) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        // 소유자 확인
        if (!product.getCreatedBy().getId().equals(userId)) {
            throw new ProductAccessDeniedException(id, userId);
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException(request.getCategoryId()));

        product.update(
                request.getName(),
                request.getDescription(),
                request.getPrice(),
                request.getStock(),
                category
        );

        return new ProductResponse(product);
    }

    @Transactional
    public void deleteProduct(Long id, Long userId) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        if (!product.getCreatedBy().getId().equals(userId)) {
            throw new ProductAccessDeniedException(id, userId);
        }

        productRepository.delete(product);
    }
}

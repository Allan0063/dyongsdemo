package com.dyongs.demo.domain.product.service;

import com.dyongs.demo.domain.product.dto.ProductRequest;
import com.dyongs.demo.domain.product.dto.ProductResponse;
import com.dyongs.demo.domain.product.entity.Product;
import com.dyongs.demo.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // Create
    public ProductResponse createProduct(ProductRequest request) {
        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .stock(request.getStock())
                .build();

        Product saved = productRepository.save(product);
        return new ProductResponse(saved);
    }

    // Read - 단일
    public ProductResponse getProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product Not Found"));
        return new ProductResponse(product);
    }

    // Read - 전체
    public List<ProductResponse> getProducts() {
        return productRepository.findAll().stream()
                .map(ProductResponse::new)
                .toList();
    }

    // Update
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product Not Found"));

        product.update(
                request.getName(),
                request.getDescription(),
                request.getPrice(),
                request.getStock()
        );

        Product updated = productRepository.save(product);
        return new ProductResponse(updated);
    }

    // Delete
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product Not Found");
        }
        productRepository.deleteById(id);
    }
}

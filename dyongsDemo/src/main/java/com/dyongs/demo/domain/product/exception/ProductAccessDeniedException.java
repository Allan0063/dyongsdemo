package com.dyongs.demo.domain.product.exception;

public class ProductAccessDeniedException extends RuntimeException {

    public ProductAccessDeniedException(Long productId, Long userId) {
        super("해당 상품에 대한 권한이 없습니다. productId=" + productId + ", userId=" + userId);
    }
}

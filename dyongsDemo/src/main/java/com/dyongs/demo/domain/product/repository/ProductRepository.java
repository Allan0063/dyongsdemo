package com.dyongs.demo.domain.product.repository;

import com.dyongs.demo.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // 직접 쿼리 메서드 만들기 가능
    @Query("SELECT MAX(p.id) FROM Product p")
    Long findMaxId();
}

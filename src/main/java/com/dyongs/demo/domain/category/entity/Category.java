package com.dyongs.demo.domain.category.entity;

import com.dyongs.demo.domain.product.entity.Product;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 카테고리명 (예: "전자제품", "식품")
    @Column(nullable = false, unique = true, length = 100)
    private String name;

    // 선택: 역방향 참조 (카테고리에 속한 상품들)
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Product> products;
}
package com.dyongs.demo.domain.product.entity;

import com.dyongs.demo.domain.category.entity.Category;
import com.dyongs.demo.domain.user.entity.User;
import com.dyongs.demo.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    private int stock;


    @ManyToOne(fetch = FetchType.LAZY) // Product 입장에서 User는 N:1 관계, fetch = FetchType.LAZY는 필요할 때만 User 정보를 가져오겠다(불필요한 JOIN을 막아 성능 최적화)
    @JoinColumn(name = "created_by") // Product 테이블에 created_by 라는 컬럼을 만들어 User의 id를 FK로 매핑
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    public void update(String name, String description, BigDecimal price, int stock, Category category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.category = category;
    }
}

package com.dyongs.demo.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;

    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    /*
    // number(10,2) 자료형의 경우
    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    // 날짜의 경우엔 LocalDate, LocalDateTime
    // Entity엔 아래처럼 선언
    private LocalDate date;

    // DTO에 아래처럼 기재하여 포맷 관리
    // @JsonFormat(pattern = "yyyyMMdd")
    // private LocalDate birthDate;
    // @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    // private LocalDateTime createdAt;
    */
}

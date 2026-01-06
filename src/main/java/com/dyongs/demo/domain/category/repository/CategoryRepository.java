package com.dyongs.demo.domain.category.repository;

import com.dyongs.demo.domain.category.dto.CategoryListResponse;
import com.dyongs.demo.domain.category.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    // ManyToOne or OneToOne
    boolean existsByName(String name);
    Optional<Category> findByName(String name);

    // 목록
    @Query(
            value = """
        select new com.dyongs.demo.domain.category.dto.CategoryListResponse(c.id, c.name)
        from Category c
        where (:keyword is null or :keyword = '')
           or lower(c.name) like lower(concat('%', :keyword, '%'))
    """,
            countQuery = """
        select count(c)
        from Category c
        where (:keyword is null or :keyword = '')
           or lower(c.name) like lower(concat('%', :keyword, '%'))
    """
    )
    Page<CategoryListResponse> searchCategoryList(@Param("keyword") String keyword, Pageable pageable);
}

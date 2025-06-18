package com.example.ecommerce.repository;

import com.example.ecommerce.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query(value = "SELECT * FROM CATEGORIES WHERE name = :name", nativeQuery = true)
    Optional<Category> findByName(@Param("name") String name);

    @Query(value = "SELECT c.*, COUNT(p.product_id) as product_count FROM CATEGORIES c " +
            "LEFT JOIN PRODUCTS p ON c.category_id = p.category_id " +
            "GROUP BY c.category_id", nativeQuery = true)
    List<Object[]> findAllWithProductCount();
}

package com.example.ecommerce.repository;

import com.example.ecommerce.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

    @Query(value = "SELECT * FROM PRODUCT_IMAGES WHERE product_id = :productId", nativeQuery = true)
    Optional<ProductImage> findByProductId(@Param("productId") Long productId);
}

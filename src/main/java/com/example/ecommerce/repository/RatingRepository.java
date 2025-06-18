package com.example.ecommerce.repository;

import com.example.ecommerce.model.Rating;
import com.example.ecommerce.model.RatingId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, RatingId> {

    @Query(value = "SELECT * FROM ratings WHERE product_id = :productId", nativeQuery = true)
    List<Rating> findByProductId(@Param("productId") Long productId);

    @Query(value = "SELECT * FROM ratings WHERE customer_id = :customerId", nativeQuery = true)
    List<Rating> findByCustomerId(@Param("customerId") Long customerId);

    @Query(value = "SELECT AVG(rating) FROM ratings WHERE product_id = :productId", nativeQuery = true)
    Double getAverageRatingForProduct(@Param("productId") Long productId);

    @Query(value = "SELECT * FROM ratings WHERE customer_id = :customerId AND product_id = :productId", nativeQuery = true)
    Optional<Rating> findByCustomerIdAndProductId(@Param("customerId") Long customerId, @Param("productId") Long productId);

    @Query(value = "SELECT AVG(r.rating) FROM ratings r " +
            "JOIN products p ON r.product_id = p.product_id " +
            "WHERE p.seller_id = :sellerId", nativeQuery = true)
    Double getAverageRatingForSeller(@Param("sellerId") Long sellerId);


    @Query(value = "SELECT AVG(rating) FROM RATINGS WHERE product_id = :productId", nativeQuery = true)
    Double getAverageRatingForProductByFilter(
            @Param("productId") Long productId
    );
    @Query(value = "SELECT AVG(r.rating) FROM RATINGS r", nativeQuery = true)
    Double getAverageRatingAllSellers();
}

package com.example.ecommerce.repository;

import com.example.ecommerce.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query(value = "SELECT * FROM CART WHERE customer_id = :customerId", nativeQuery = true)
    Optional<Cart> findByCustomerId(@Param("customerId") Long customerId);

    @Query(value = "SELECT SUM(ci.total_price) FROM CART c " +
            "JOIN CART_ITEM ci ON c.cart_id = ci.cart_id " +
            "WHERE c.cart_id = :cartId", nativeQuery = true)
    BigDecimal calculateCartTotal(@Param("cartId") Long cartId);
}

package com.example.ecommerce.repository;

import com.example.ecommerce.model.CartItem;
import com.example.ecommerce.model.CartItemId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, CartItemId> {

    @Query(value = "SELECT * FROM CART_ITEM WHERE cart_id = :cartId", nativeQuery = true)
    List<CartItem> findByCartId(@Param("cartId") Long cartId);

    @Query(value = "DELETE FROM CART_ITEM WHERE cart_id = :cartId AND product_id = :productId", nativeQuery = true)
    void deleteCartItem(@Param("cartId") Long cartId, @Param("productId") Long productId);

    @Query(value = "UPDATE CART_ITEM SET quantity = :quantity, total_price = :totalPrice " +
            "WHERE cart_id = :cartId AND product_id = :productId", nativeQuery = true)
    void updateCartItem(@Param("cartId") Long cartId, @Param("productId") Long productId,
                        @Param("quantity") Integer quantity, @Param("totalPrice") BigDecimal totalPrice);
    @Modifying
    @Query(
            value = "DELETE FROM CART_ITEM WHERE cart_id = :cartId",
            nativeQuery = true
    )
    void deleteByCartId(@Param("cartId") Long cartId);
}

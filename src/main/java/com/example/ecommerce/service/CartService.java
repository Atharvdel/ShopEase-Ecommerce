package com.example.ecommerce.service;

import com.example.ecommerce.model.Cart;
import java.math.BigDecimal;
import java.util.Optional;

public interface CartService {
    Cart saveCart(Cart cart);
    Optional<Cart> getCartById(Long id);
    Optional<Cart> getCartByCustomerId(Long customerId);
    BigDecimal calculateCartTotal(Long cartId);
    void deleteCart(Long id);
    void removeItemFromCart(Long cartId, Long productId);
}

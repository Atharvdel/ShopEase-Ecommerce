package com.example.ecommerce.service;

import com.example.ecommerce.model.CartItem;
import com.example.ecommerce.model.CartItemId;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CartItemService {
    CartItem saveCartItem(CartItem cartItem);
    List<CartItem> getAllCartItems();
    Optional<CartItem> getCartItemById(CartItemId id);
    List<CartItem> getCartItemsByCartId(Long cartId);
    void updateCartItemQuantity(Long cartId, Long productId, Integer quantity, BigDecimal totalPrice);
    void deleteCartItem(CartItemId id);
    void deleteCartItemsByCartId(Long cartId);
    void deleteByCartId(Long cartId);
}

package com.example.ecommerce.service.impl;

import com.example.ecommerce.model.CartItem;
import com.example.ecommerce.model.CartItemId;
import com.example.ecommerce.repository.CartItemRepository;
import com.example.ecommerce.service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;

    @Autowired
    public CartItemServiceImpl(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public CartItem saveCartItem(CartItem cartItem) {
        return cartItemRepository.save(cartItem);
    }

    @Override
    public List<CartItem> getAllCartItems() {
        return cartItemRepository.findAll();
    }

    @Override
    public Optional<CartItem> getCartItemById(CartItemId id) {
        return cartItemRepository.findById(id);
    }

    @Override
    public List<CartItem> getCartItemsByCartId(Long cartId) {
        return cartItemRepository.findByCartId(cartId);
    }

    @Override
    public void updateCartItemQuantity(Long cartId, Long productId, Integer quantity, BigDecimal totalPrice) {
        cartItemRepository.updateCartItem(cartId, productId, quantity, totalPrice);
    }

    @Override
    public void deleteCartItem(CartItemId id) {
        cartItemRepository.deleteById(id);
    }

    @Override
    public void deleteCartItemsByCartId(Long cartId) {
        List<CartItem> cartItems = cartItemRepository.findByCartId(cartId);
        cartItemRepository.deleteAll(cartItems);
    }
    @Override
    public void deleteByCartId(Long cartId) {
        cartItemRepository.deleteByCartId(cartId);
    }
}

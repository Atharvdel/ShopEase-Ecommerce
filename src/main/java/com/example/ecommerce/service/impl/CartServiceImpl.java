package com.example.ecommerce.service.impl;

import com.example.ecommerce.model.Cart;
import com.example.ecommerce.repository.CartRepository;
import com.example.ecommerce.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.Optional;
import com.example.ecommerce.model.CartItem;
import com.example.ecommerce.repository.CartItemRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository, CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public Cart saveCart(Cart cart) {
        return cartRepository.save(cart);
    }

    @Override
    public Optional<Cart> getCartById(Long id) {
        return cartRepository.findById(id);
    }

    @Override
    public Optional<Cart> getCartByCustomerId(Long customerId) {
        return cartRepository.findByCustomerId(customerId);
    }

    @Override
    public BigDecimal calculateCartTotal(Long cartId) {
        return cartRepository.calculateCartTotal(cartId);
    }

    @Override
    public void deleteCart(Long id) {
        cartRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void removeItemFromCart(Long cartId, Long productId) {
        // Find all cart items for this cart
        List<CartItem> cartItems = cartItemRepository.findByCartId(cartId);

        // Find the cart item with the matching product ID
        for (CartItem item : cartItems) {
            if (item.getProduct().getProductId().equals(productId)) {
                // Remove the item
                cartItemRepository.delete(item);
                break;
            }
        }
    }
}

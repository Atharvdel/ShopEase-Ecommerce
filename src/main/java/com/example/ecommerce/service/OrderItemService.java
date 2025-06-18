package com.example.ecommerce.service;

import com.example.ecommerce.model.OrderItem;
import com.example.ecommerce.model.OrderItemId;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface OrderItemService {
    OrderItem saveOrderItem(OrderItem orderItem);
    List<OrderItem> getAllOrderItems();
    Optional<OrderItem> getOrderItemById(OrderItemId id);
    List<OrderItem> getOrderItemsByOrderId(Long orderId);
    List<Object[]> getSalesByCategory();
    void deleteOrderItem(OrderItemId id);
    BigDecimal getTotalSalesAllSellers();
    Long getTotalOrdersAllSellers();
    List<Object[]> getTopSellers();
    List<Object[]> getCategoryWiseSalesAllSellers();
    List<Object[]> getTopSellingProducts();
    List<Object[]> getActiveProducts();

    // NEW: For seller dashboard
    List<OrderItem> findOrderItemsBySellerId(Long sellerId);
}

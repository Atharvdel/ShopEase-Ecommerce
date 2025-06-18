package com.example.ecommerce.service.impl;

import com.example.ecommerce.model.OrderItem;
import com.example.ecommerce.model.OrderItemId;
import com.example.ecommerce.repository.OrderItemRepository;
import com.example.ecommerce.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;

    @Autowired
    public OrderItemServiceImpl(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public OrderItem saveOrderItem(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }

    @Override
    public List<OrderItem> getAllOrderItems() {
        return orderItemRepository.findAll();
    }

    @Override
    public Optional<OrderItem> getOrderItemById(OrderItemId id) {
        return orderItemRepository.findById(id);
    }

    @Override
    public List<OrderItem> getOrderItemsByOrderId(Long orderId) {
        return orderItemRepository.findByOrderId(orderId);
    }

    @Override
    public List<Object[]> getSalesByCategory() {
        return orderItemRepository.getSalesByCategory();
    }

    @Override
    public void deleteOrderItem(OrderItemId id) {
        orderItemRepository.deleteById(id);
    }

    @Override
    public List<OrderItem> findOrderItemsBySellerId(Long sellerId) {
        return orderItemRepository.findOrderItemsBySellerId(sellerId);
    }
    @Override
    public BigDecimal getTotalSalesAllSellers() {
        return orderItemRepository.sumTotalSalesAllSellers();
    }

    @Override
    public Long getTotalOrdersAllSellers() {
        return orderItemRepository.countTotalOrdersAllSellers();
    }

    @Override
    public List<Object[]> getTopSellers() {
        return orderItemRepository.findTopSellers();
    }

    @Override
    public List<Object[]> getCategoryWiseSalesAllSellers() {
        return orderItemRepository.findCategoryWiseSalesAllSellers();
    }

    @Override
    public List<Object[]> getActiveProducts() {
        return orderItemRepository.findActiveProducts(); // Now returns List<Object[]>
    }
    @Override
    public List<Object[]> getTopSellingProducts() {
        return orderItemRepository.findTopSellingProducts();
    }
}

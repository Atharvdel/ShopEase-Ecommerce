package com.example.ecommerce.service;

import java.math.BigDecimal;
import java.util.List;

public interface SellerAnalyticsService {
    // Seller-level analytics
    BigDecimal getTotalSales(Long sellerId);
    Long getTotalOrders(Long sellerId);
    Double getAverageRating(Long sellerId);
    List<Object[]> getTopSellingProducts(Long sellerId);
    List<Object[]> getDailySales(Long sellerId);
    List<Object[]> getCategoryWiseSales(Long sellerId);

    // Product-level analytics
    int getUnitsSoldForProduct(Long productId, String filterType);
    double getAverageRatingForProduct(Long productId, String filterType);
    BigDecimal getTotalSalesForProduct(Long productId, String filterType);
}

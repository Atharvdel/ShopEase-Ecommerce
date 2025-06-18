package com.example.ecommerce.service.impl;

import com.example.ecommerce.repository.OrderItemRepository;
import com.example.ecommerce.repository.RatingRepository;
import com.example.ecommerce.service.SellerAnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class SellerAnalyticsServiceImpl implements SellerAnalyticsService {

    private final OrderItemRepository orderItemRepository;
    private final RatingRepository ratingRepository;

    @Autowired
    public SellerAnalyticsServiceImpl(OrderItemRepository orderItemRepository,
                                      RatingRepository ratingRepository) {
        this.orderItemRepository = orderItemRepository;
        this.ratingRepository = ratingRepository;
    }

    // Seller-level
    @Override
    public BigDecimal getTotalSales(Long sellerId) {
        BigDecimal total = orderItemRepository.getTotalSalesForSeller(sellerId);
        return total != null ? total : BigDecimal.ZERO;
    }

    @Override
    public Long getTotalOrders(Long sellerId) {
        Long count = orderItemRepository.getTotalOrdersForSeller(sellerId);
        return count != null ? count : 0L;
    }

    @Override
    public Double getAverageRating(Long sellerId) {
        Double avg = ratingRepository.getAverageRatingForSeller(sellerId);
        return avg != null ? avg : 0.0;
    }

    @Override
    public List<Object[]> getTopSellingProducts(Long sellerId) {
        return orderItemRepository.getTopSellingProductsForSeller(sellerId);
    }

    @Override
    public List<Object[]> getDailySales(Long sellerId) {
        return orderItemRepository.getDailySalesForSeller(sellerId);
    }

    @Override
    public List<Object[]> getCategoryWiseSales(Long sellerId) {
        return orderItemRepository.getCategoryWiseSalesForSeller(sellerId);
    }

    // Product-level
    @Override
    public int getUnitsSoldForProduct(Long productId, String filterType) {
        LocalDate[] dates = calculateDateRange(filterType);
        Integer units = orderItemRepository.findUnitsSoldByProductAndFilter(productId, dates[0], dates[1]);
        return units != null ? units : 0;
    }

    @Override
    public double getAverageRatingForProduct(Long productId, String filterType) {
        LocalDate[] dates = calculateDateRange(filterType);
        Double avg = ratingRepository.getAverageRatingForProductByFilter(productId);
        return avg != null ? avg : 0.0;
    }

    @Override
    public BigDecimal getTotalSalesForProduct(Long productId, String filterType) {
        LocalDate[] dates = calculateDateRange(filterType);
        BigDecimal total = orderItemRepository.getTotalSalesForProductByFilter(productId, dates[0], dates[1]);
        return total != null ? total : BigDecimal.ZERO;
    }

    private LocalDate[] calculateDateRange(String filterType) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate;
        switch (filterType.toLowerCase()) {
            case "daily": startDate = endDate; break;
            case "weekly": startDate = endDate.minusWeeks(1); break;
            case "monthly": startDate = endDate.minusMonths(1); break;
            default: startDate = LocalDate.of(2000, 1, 1); // All-time
        }
        return new LocalDate[]{startDate, endDate};
    }
}

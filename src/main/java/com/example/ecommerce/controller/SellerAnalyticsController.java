package com.example.ecommerce.controller;

import com.example.ecommerce.model.OrderItem;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.RatingRepository;
import com.example.ecommerce.service.OrderItemService;
import com.example.ecommerce.service.SellerAnalyticsService;
import com.example.ecommerce.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/seller/analytics")
public class SellerAnalyticsController {

    @Autowired
    private SellerService sellerService;

    @Autowired
    private SellerAnalyticsService sellerAnalyticsService;

    @Autowired
    private RatingRepository ratingRepository;

    // Add this if you need OrderItemService
    @Autowired
    private OrderItemService orderItemService;

    private Long getSellerId(Authentication authentication) {
        String email = authentication.getName();
        return sellerService.getSellerByEmail(email)
                .orElseThrow(() -> new RuntimeException("Seller not found")).getId();
    }

    @GetMapping("/data")
    @ResponseBody
    public Map<String, Object> getAnalyticsData(
            @RequestParam String type,
            Authentication authentication
    ) {
        Long sellerId = getSellerId(authentication);
        Map<String, Object> response = new HashMap<>();

        switch (type) {
            case "total-sales":
                BigDecimal totalSales = sellerAnalyticsService.getTotalSales(sellerId);

                // Get all orders for this seller's products
                List<OrderItem> orderItems = orderItemService.findOrderItemsBySellerId(sellerId);

                // Create a table: Order ID | Amount
                response.put("title", "Total Sales: ₹" + totalSales);
                response.put("tableHeaders", List.of("Order ID", "Amount (₹)"));
                response.put("tableData", orderItems.stream()
                        .map(item -> List.of(
                                item.getOrder().getOrderId(),
                                item.getTotalPrice() != null ? "₹" + item.getTotalPrice() : "N/A"
                        ))
                        .collect(Collectors.toList()));
                break;

            case "total-orders":
                Long totalOrders = sellerAnalyticsService.getTotalOrders(sellerId);
                List<OrderItem> orders = orderItemService.findOrderItemsBySellerId(sellerId);
                response.put("title", "Total Orders: " + totalOrders);
                response.put("tableHeaders", List.of("Order ID", "Product", "Quantity"));
                response.put("tableData", orders.stream()
                        .map(item -> List.of(
                                item.getOrder().getOrderId(),
                                item.getProduct().getName(),
                                item.getQuantity()
                        )).collect(Collectors.toList()));
                break;

            case "active-products":
                List<Product> products = sellerService.getSellerProducts(sellerId);
                response.put("title", "Active Products: " + products.size());
                response.put("tableHeaders", List.of("Product ID", "Name", "Price", "Avg Rating"));
                response.put("tableData", products.stream()
                        .map(p -> {
                            Double rating = ratingRepository.getAverageRatingForProduct(p.getProductId());
                            return List.of(
                                    p.getProductId(),
                                    p.getName(),
                                    "₹" + p.getPrice(),
                                    rating != null ? String.format("%.1f ⭐", rating) : "N/A"
                            );
                        })
                        .collect(Collectors.toList()));
                break;

            case "overall-ratings":
                Double sellerRating = sellerAnalyticsService.getAverageRating(sellerId);
                List<Product> sellerProducts = sellerService.getSellerProducts(sellerId);

                response.put("title", "Overall Average Rating: " +
                        (sellerRating != null ? String.format("%.1f ⭐", sellerRating) : "N/A"));

                response.put("tableHeaders", List.of("Product ID", "Product Name", "Average Rating"));
                response.put("tableData", sellerProducts.stream()
                        .map(p -> {
                            Double productRating = ratingRepository.getAverageRatingForProduct(p.getProductId());
                            return List.of(
                                    p.getProductId(),
                                    p.getName(),
                                    productRating != null ? String.format("%.1f ⭐", productRating) : "N/A"
                            );
                        })
                        .collect(Collectors.toList()));
                break;

            case "top-products":
                List<Object[]> topProducts = sellerAnalyticsService.getTopSellingProducts(sellerId);
                response.put("title", "Top Selling Products");
                response.put("tableHeaders", List.of("Product", "Units Sold"));
                response.put("tableData", topProducts.stream()
                        .map(row -> List.of(row[0], row[1]))
                        .collect(Collectors.toList()));
                break;

            case "category-sales":
                List<Object[]> categorySales = sellerAnalyticsService.getCategoryWiseSales(sellerId);
                response.put("title", "Category-wise Sales");
                response.put("tableHeaders", List.of("Category", "Total Sales"));
                response.put("tableData", categorySales.stream()
                        .map(row -> List.of(row[0], "₹" + row[1]))
                        .collect(Collectors.toList()));
                break;
        }

        return response;
    }
}

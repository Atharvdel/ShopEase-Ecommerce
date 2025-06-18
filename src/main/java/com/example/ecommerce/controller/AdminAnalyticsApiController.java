package com.example.ecommerce.controller;

import com.example.ecommerce.model.Seller;
import com.example.ecommerce.service.OrderItemService;
import com.example.ecommerce.service.SellerService;
import com.example.ecommerce.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/analytics")
public class AdminAnalyticsApiController {

    private final SellerService sellerService;
    private final OrderItemService orderItemService;
    private final RatingRepository ratingRepository;

    @Autowired
    public AdminAnalyticsApiController(
            SellerService sellerService,
            OrderItemService orderItemService,
            RatingRepository ratingRepository
    ) {
        this.sellerService = sellerService;
        this.orderItemService = orderItemService;
        this.ratingRepository = ratingRepository;
    }

    @GetMapping("/data")
    public Map<String, Object> getAnalyticsData(@RequestParam String type) {
        Map<String, Object> response = new HashMap<>();

        switch (type.toLowerCase()) {
            case "total-sales":
                BigDecimal totalSales = orderItemService.getTotalSalesAllSellers();
                response.put("title", "Total Sales: ₹" + totalSales);
                response.put("value", "₹" + totalSales);
                break;

            case "total-orders":
                Long totalOrders = orderItemService.getTotalOrdersAllSellers();
                response.put("title", "Total Orders: " + totalOrders);
                response.put("value", totalOrders);
                break;

            case "active-sellers":
                List<Seller> sellers = sellerService.getAllSellers();
                response.put("title", "Active Sellers: " + sellers.size());
                response.put("tableHeaders", List.of("Seller ID", "Business Name", "Email"));
                response.put("tableData", sellers.stream()
                        .map(s -> List.of(s.getId(), s.getBusinessName(), s.getEmail()))
                        .collect(Collectors.toList()));
                break;

            case "overall-ratings":
                Double avgRating = ratingRepository.getAverageRatingAllSellers();
                response.put("title", "Overall Average Rating: " +
                        (avgRating != null ? String.format("%.1f ⭐", avgRating) : "N/A"));
                response.put("value", avgRating != null ? String.format("%.1f ⭐", avgRating) : "N/A");
                break;

            case "top-sellers":
                List<Object[]> topSellers = orderItemService.getTopSellers();
                response.put("title", "Top Sellers");
                response.put("tableHeaders", List.of("Seller", "Total Sales"));
                response.put("tableData", topSellers.stream()
                        .map(row -> List.of(row[0], "₹" + row[1]))
                        .collect(Collectors.toList()));
                break;

            case "category-sales":
                List<Object[]> categorySales = orderItemService.getCategoryWiseSalesAllSellers();
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

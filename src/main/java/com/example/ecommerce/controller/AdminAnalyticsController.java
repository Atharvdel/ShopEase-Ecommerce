package com.example.ecommerce.controller;

import com.example.ecommerce.model.*;
import com.example.ecommerce.repository.RatingRepository;
import com.example.ecommerce.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/analytics")
public class AdminAnalyticsController {

    @Autowired
    private SellerService sellerService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private RatingRepository ratingRepository;

    @GetMapping("/data")
    public Map<String, Object> getAnalyticsData(@RequestParam String type) {
        Map<String, Object> response = new HashMap<>();

        switch (type) {
            case "total-sales":
                List<Order> orders = orderService.getAllOrders();
                response.put("title", "All Orders with Customer Details");
                response.put("tableHeaders", List.of("Order ID", "Customer Name", "Amount (₹)", "Order Date"));
                response.put("tableData", orders.stream()
                        .map(order -> List.of(
                                order.getOrderId(),
                                order.getCustomer().getName(),
                                "₹" + order.getAmount(),
                                order.getTime().toString()
                        ))
                        .collect(Collectors.toList()));
                break;

            case "total-orders":
                List<Customer> customers = customerService.getAllCustomers();
                Map<Long, Long> orderCounts = orderService.getOrderCountByCustomer();
                response.put("title", "Customer Order Counts");
                response.put("tableHeaders", List.of("Customer Name", "Email", "Total Orders"));
                response.put("tableData", customers.stream()
                        .map(c -> List.of(
                                c.getName(),
                                c.getEmail(),
                                orderCounts.getOrDefault(c.getId(), 0L)
                        ))
                        .collect(Collectors.toList()));
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
                List<Seller> allSellers = sellerService.getAllSellers();

                response.put("title", "Overall Average Rating: " +
                        (avgRating != null ? String.format("%.1f ⭐", avgRating) : "N/A"));

                response.put("tableHeaders", List.of("Seller Name", "Email", "Average Rating"));

                // For each seller, get their average rating from the repository
                response.put("tableData", allSellers.stream()
                        .map(seller -> {
                            Double sellerAvg = ratingRepository.getAverageRatingForSeller(seller.getId());
                            String ratingStr = sellerAvg != null ? String.format("%.1f ⭐", sellerAvg) : "N/A";
                            return List.of(seller.getBusinessName(), seller.getEmail(), ratingStr);
                        })
                        .collect(Collectors.toList()));
                break;

            case "top-sellers":
                List<Object[]> topSellers = orderItemService.getTopSellers();
                response.put("title", "Top Performing Sellers");
                response.put("tableHeaders", List.of("Seller", "Total Sales"));
                response.put("tableData", topSellers.stream()
                        .map(row -> List.of(row[0], "₹" + row[1]))
                        .collect(Collectors.toList()));
                break;

            case "category-sales":
                List<Object[]> categorySales = orderItemService.getCategoryWiseSalesAllSellers();
                response.put("title", "Category-wise Sales Distribution");
                response.put("tableHeaders", List.of("Category", "Total Sales"));
                response.put("tableData", categorySales.stream()
                        .map(row -> List.of(row[0], "₹" + row[1]))
                        .collect(Collectors.toList()));
                break;
        }
        return response;
    }
}

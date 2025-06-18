package com.example.ecommerce.controller;

import com.example.ecommerce.model.OrderItem;
import com.example.ecommerce.service.OrderItemService;
import com.example.ecommerce.service.SellerService;
import com.example.ecommerce.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/selleranalytics")
public class AdminSellerAnalyticsController {

    private final SellerService sellerService;
    private final OrderItemService orderItemService;
    private final RatingRepository ratingRepository;

    @Autowired
    public AdminSellerAnalyticsController(
            SellerService sellerService,
            OrderItemService orderItemService,
            RatingRepository ratingRepository
    ) {
        this.sellerService = sellerService;
        this.orderItemService = orderItemService;
        this.ratingRepository = ratingRepository;
    }

    @GetMapping
    public String getSellerAnalytics(
            @RequestParam(value = "type", required = false) String type,
            Model model
    ) {
        // Default title if no type is selected
        String title = "Admin Seller Analytics";
        model.addAttribute("analyticsTitle", title);

        if (type != null) {
            switch (type.toLowerCase()) {
                case "total-sales":
                    BigDecimal totalSales = orderItemService.getTotalSalesAllSellers();
                    model.addAttribute("analyticsTitle", "Total Sales");
                    model.addAttribute("analyticsValue", "₹" + totalSales);
                    break;

                case "total-orders":
                    Long totalOrders = orderItemService.getTotalOrdersAllSellers();
                    model.addAttribute("analyticsTitle", "Total Orders");
                    model.addAttribute("analyticsValue", totalOrders.toString());
                    break;

                case "active-products":
                    List<Object[]> activeProducts = orderItemService.getActiveProducts();
                    model.addAttribute("analyticsTitle", "Active Products");
                    model.addAttribute("tableHeaders", List.of("Product ID", "Name", "Total Sold"));
                    model.addAttribute("tableData", activeProducts.stream()
                            .map(row -> List.of(
                                    row[0].toString(), // productId
                                    row[1].toString(), // name
                                    row[2].toString()  // total sold
                            ))
                            .collect(Collectors.toList()));
                    break;

                case "overall-ratings":
                    Double avgRating = ratingRepository.getAverageRatingAllSellers();
                    model.addAttribute("analyticsTitle", "Overall Ratings");
                    model.addAttribute("analyticsValue",
                            avgRating != null ? String.format("%.1f ⭐", avgRating) : "N/A");
                    break;

                case "top-products":
                    List<Object[]> topProducts = orderItemService.getTopSellingProducts();
                    model.addAttribute("analyticsTitle", "Top Selling Products");
                    model.addAttribute("tableHeaders", List.of("Product", "Units Sold"));
                    model.addAttribute("tableData", topProducts.stream()
                            .map(row -> List.of(row[0], row[1]))
                            .collect(Collectors.toList()));
                    break;

                case "category-sales":
                    List<Object[]> categorySales = orderItemService.getCategoryWiseSalesAllSellers();
                    model.addAttribute("analyticsTitle", "Category-wise Sales");
                    model.addAttribute("tableHeaders", List.of("Category", "Total Sales"));
                    model.addAttribute("tableData", categorySales.stream()
                            .map(row -> List.of(row[0], "₹" + row[1]))
                            .collect(Collectors.toList()));
                    break;

                default:
                    model.addAttribute("analyticsTitle", "Invalid Analytics Type");
            }
        }

        return "adminselleranalytics";
    }
}

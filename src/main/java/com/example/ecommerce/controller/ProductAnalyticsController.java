package com.example.ecommerce.controller;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.ProductImage;
import com.example.ecommerce.service.ProductService;
import com.example.ecommerce.service.ProductImageService;
import com.example.ecommerce.service.SellerAnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.math.BigDecimal;

@Controller
public class ProductAnalyticsController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductImageService productImageService;

    @Autowired
    private SellerAnalyticsService sellerAnalyticsService;

    @GetMapping("/productanalytics.html")
    public String productAnalytics(
            @RequestParam("productId") Long productId,
            @RequestParam(value = "filter", required = false, defaultValue = "overall") String filterType,
            Model model
    ) {
        Product product = productService.getProductById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Use imageUrl directly from product
        String imageUrl = product.getImageUrl();

        int unitsSold = sellerAnalyticsService.getUnitsSoldForProduct(productId, filterType);
        double averageRating = sellerAnalyticsService.getAverageRatingForProduct(productId, filterType);
        BigDecimal totalSales = sellerAnalyticsService.getTotalSalesForProduct(productId, filterType);

        model.addAttribute("product", product);
        model.addAttribute("imageUrl", imageUrl);
        model.addAttribute("unitsSold", unitsSold);
        model.addAttribute("averageRating", averageRating);
        model.addAttribute("totalSales", totalSales);
        model.addAttribute("filterType", filterType.substring(0, 1).toUpperCase() + filterType.substring(1));

        return "productanalytics";
    }
}

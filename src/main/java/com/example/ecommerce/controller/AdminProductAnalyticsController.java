package com.example.ecommerce.controller;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.service.ProductService;
import com.example.ecommerce.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/adminproductanalytics.html")
public class AdminProductAnalyticsController {

    private final ProductService productService;
    private final RatingService ratingService;

    @Autowired
    public AdminProductAnalyticsController(ProductService productService, RatingService ratingService) {
        this.productService = productService;
        this.ratingService = ratingService;
    }

    @GetMapping
    public String viewAllProductAnalytics(Model model) {
        List<Product> products = productService.getAllProducts();

        // Add average ratings
        Map<Long, Double> productRatings = new HashMap<>();
        for (Product product : products) {
            Double avgRating = ratingService.getAverageRatingForProduct(product.getProductId());
            productRatings.put(product.getProductId(), avgRating);
        }
        model.addAttribute("products", products);
        model.addAttribute("productRatings", productRatings);

        return "adminproductanalytics";
    }
}

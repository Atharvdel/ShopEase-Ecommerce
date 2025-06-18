package com.example.ecommerce.controller;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.ProductImage;
import com.example.ecommerce.model.Seller;
import com.example.ecommerce.service.ProductImageService;
import com.example.ecommerce.service.ProductService;
import com.example.ecommerce.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class SellerHomeController {

    private final SellerService sellerService;
    private final ProductService productService;
    private final ProductImageService productImageService;

    @Autowired
    public SellerHomeController(SellerService sellerService, ProductService productService, ProductImageService productImageService) {
        this.sellerService = sellerService;
        this.productService = productService;
        this.productImageService = productImageService;
    }

    @GetMapping("/sellerhomepage.html")
    public String sellerHomepage(
            Authentication authentication,
            @RequestParam(value = "keyword", required = false) String keyword,
            Model model) {

        // Get seller info (assuming you use email as authentication name)
        Seller seller = sellerService.getSellerByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Seller not found"));

        List<Product> sellerProducts;
        if (keyword != null && !keyword.trim().isEmpty()) {
            // Search products by keyword for this seller
            sellerProducts = productService.searchSellerProductsByKeyword(seller.getId(), keyword);
        } else {
            // Get all products for this seller
            sellerProducts = productService.getProductsBySellerId(seller.getId());
        }

        model.addAttribute("seller", seller);
        model.addAttribute("sellerProducts", sellerProducts);
        model.addAttribute("keyword", keyword);

        return "sellerhomepage";
    }
}

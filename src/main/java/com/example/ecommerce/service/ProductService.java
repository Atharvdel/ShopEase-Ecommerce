package com.example.ecommerce.service;

import com.example.ecommerce.model.Product;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductService {
    Product saveProduct(Product product);
    List<Product> getAllProducts();
    Optional<Product> getProductById(Long id);
    List<Product> getProductsByCategoryId(Long categoryId);
    List<Product> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);
    List<Product> searchProductsByKeyword(String keyword);
    List<Product> getAllProductsOrderByPriceAsc();
    List<Product> getAllProductsOrderByPriceDesc();
    List<Product> getTopSellingProducts();
    void deleteProduct(Long id);
    List<Product> getProductsBySellerId(Long sellerId);
    List<Product> searchSellerProductsByKeyword(Long sellerId, String keyword);

}

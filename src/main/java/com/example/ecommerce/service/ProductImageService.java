package com.example.ecommerce.service;

import com.example.ecommerce.model.ProductImage;
import java.util.List;
import java.util.Optional;

public interface ProductImageService {
    ProductImage saveProductImage(ProductImage productImage);
    List<ProductImage> getAllProductImages();
    Optional<ProductImage> getProductImageByProductId(Long productId);
    void deleteProductImage(Long productId);
}

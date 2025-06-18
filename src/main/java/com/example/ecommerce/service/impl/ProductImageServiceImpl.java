package com.example.ecommerce.service.impl;

import com.example.ecommerce.model.ProductImage;
import com.example.ecommerce.repository.ProductImageRepository;
import com.example.ecommerce.service.ProductImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProductImageServiceImpl implements ProductImageService {

    private final ProductImageRepository productImageRepository;

    @Autowired
    public ProductImageServiceImpl(ProductImageRepository productImageRepository) {
        this.productImageRepository = productImageRepository;
    }

    @Override
    public ProductImage saveProductImage(ProductImage productImage) {
        return productImageRepository.save(productImage);
    }

    @Override
    public List<ProductImage> getAllProductImages() {
        return productImageRepository.findAll();
    }

    @Override
    public Optional<ProductImage> getProductImageByProductId(Long productId) {
        return productImageRepository.findByProductId(productId);
    }

    @Override
    public void deleteProductImage(Long productId) {
        productImageRepository.findByProductId(productId)
                .ifPresent(productImageRepository::delete);
    }
}

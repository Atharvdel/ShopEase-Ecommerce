package com.example.ecommerce.service.impl;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public List<Product> getProductsByCategoryId(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    @Override
    public List<Product> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepository.findByPriceRange(minPrice, maxPrice);
    }

    @Override
    public List<Product> searchProductsByKeyword(String keyword) {
        return productRepository.searchByKeyword(keyword);
    }

    @Override
    public List<Product> getAllProductsOrderByPriceAsc() {
        return productRepository.findAllOrderByPriceAsc();
    }

    @Override
    public List<Product> getAllProductsOrderByPriceDesc() {
        return productRepository.findAllOrderByPriceDesc();
    }

    @Override
    public List<Product> getTopSellingProducts() {
        return productRepository.findTopSellingProducts();
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public List<Product> getProductsBySellerId(Long sellerId) {
        return productRepository.findBySellerId(sellerId);
    }

    public List<Product> searchSellerProductsByKeyword(Long sellerId, String keyword) {
        return productRepository.findBySellerIdAndKeyword(sellerId, keyword);
    }
}

package com.example.ecommerce.service;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.Seller;
import java.util.List;
import java.util.Optional;

public interface SellerService {
    Seller saveSeller(Seller seller);
    List<Seller> getAllSellers();
    Optional<Seller> getSellerById(Long id);
    Optional<Seller> getSellerByEmail(String email);
    Optional<Seller> getSellerByBusinessName(String businessName);
    List<Seller> getSellersByAdminId(Long adminId);
    void assignSellerToAdmin(Long sellerId, Long adminId);
    void removeSeller(Long id);
    List<Product> getSellerProducts(Long sellerId);
}

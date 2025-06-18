package com.example.ecommerce.service.impl;

import com.example.ecommerce.model.Admin;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.Seller;
import com.example.ecommerce.repository.AdminRepository;
import com.example.ecommerce.repository.SellerRepository;
import com.example.ecommerce.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

import com.example.ecommerce.repository.ProductRepository;

@Service
public class SellerServiceImpl implements SellerService {

    private final SellerRepository sellerRepository;
    private final AdminRepository adminRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SellerServiceImpl(SellerRepository sellerRepository,
                             AdminRepository adminRepository,
                             ProductRepository productRepository,
                             PasswordEncoder passwordEncoder) {
        this.sellerRepository = sellerRepository;
        this.adminRepository = adminRepository;
        this.productRepository = productRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Seller saveSeller(Seller seller) {
        seller.setPassword(passwordEncoder.encode(seller.getPassword()));
        return sellerRepository.save(seller);
    }

    @Override
    public List<Seller> getAllSellers() {
        return sellerRepository.findAll();
    }

    @Override
    public Optional<Seller> getSellerById(Long id) {
        return sellerRepository.findById(id);
    }

    @Override
    public Optional<Seller> getSellerByEmail(String email) {
        return sellerRepository.findByEmail(email);
    }

    @Override
    public Optional<Seller> getSellerByBusinessName(String businessName) {
        return sellerRepository.findByBusinessName(businessName);
    }

    @Override
    public List<Seller> getSellersByAdminId(Long adminId) {
        return sellerRepository.findByAdminId(adminId);
    }

    @Override
    public List<Product> getSellerProducts(Long sellerId) {
        return productRepository.findBySellerId(sellerId);
    }

    @Override
    @Transactional
    public void assignSellerToAdmin(Long sellerId, Long adminId) {
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new RuntimeException("Seller not found with id: " + sellerId));

        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found with id: " + adminId));

        admin.addSeller(seller);
        adminRepository.save(admin);
    }

    @Override
    public void removeSeller(Long id) {
        sellerRepository.deleteById(id);
    }
}

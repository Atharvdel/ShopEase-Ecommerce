package com.example.ecommerce.repository;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long> {

    @Query(value = "SELECT * FROM SELLER WHERE email = :email", nativeQuery = true)
    Optional<Seller> findByEmail(@Param("email") String email);

    @Query(value = "SELECT * FROM SELLER WHERE business_name = :businessName", nativeQuery = true)
    Optional<Seller> findByBusinessName(@Param("businessName") String businessName);

    @Query(value = "SELECT * FROM SELLER WHERE admin_id = :adminId", nativeQuery = true)
    List<Seller> findByAdminId(@Param("adminId") Long adminId);

    @Query(value = "SELECT * FROM PRODUCT WHERE seller_id = :sellerId", nativeQuery = true)
    List<Product> findBySellerId(@Param("sellerId") Long sellerId);
}

package com.example.ecommerce.repository;

import com.example.ecommerce.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query(value = "SELECT * FROM PAYMENTS WHERE order_id = :orderId", nativeQuery = true)
    Optional<Payment> findByOrderId(@Param("orderId") Long orderId);

    @Query(value = "SELECT payment_method, COUNT(*) as count, SUM(amount) as total " +
            "FROM PAYMENTS " +
            "GROUP BY payment_method", nativeQuery = true)
    List<Object[]> getPaymentMethodStats();
}

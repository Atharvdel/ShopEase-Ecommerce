package com.example.ecommerce.service;

import com.example.ecommerce.model.Payment;
import java.util.List;
import java.util.Optional;

public interface PaymentService {
    Payment savePayment(Payment payment);
    List<Payment> getAllPayments();
    Optional<Payment> getPaymentById(Long id);
    Optional<Payment> getPaymentByOrderId(Long orderId);
    List<Object[]> getPaymentMethodStats();
    void deletePayment(Long id);
}

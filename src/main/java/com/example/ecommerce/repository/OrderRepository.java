package com.example.ecommerce.repository;

import com.example.ecommerce.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(value = "SELECT * FROM ORDERS WHERE customer_id = :customerId", nativeQuery = true)
    List<Order> findByCustomerId(@Param("customerId") Long customerId);

    @Query(value = "SELECT * FROM ORDERS WHERE time BETWEEN :startDate AND :endDate", nativeQuery = true)
    List<Order> findByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query(value = "SELECT SUM(amount) FROM ORDERS WHERE time BETWEEN :startDate AND :endDate", nativeQuery = true)
    BigDecimal calculateTotalSalesForPeriod(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query(value = "SELECT MONTH(time) as month, SUM(amount) as total " +
            "FROM ORDERS " +
            "WHERE YEAR(time) = :year " +
            "GROUP BY MONTH(time) " +
            "ORDER BY month", nativeQuery = true)
    List<Object[]> getMonthlySales(@Param("year") Integer year);

    @Query(value = "SELECT SUM(amount) FROM ORDERS", nativeQuery = true)
    BigDecimal getTotalSalesForAllSellers();

    @Query("SELECT o FROM Order o JOIN FETCH o.customer")
    List<Order> findAllWithCustomer();

    @Query("SELECT o.customer.id, COUNT(o) FROM Order o GROUP BY o.customer.id")
    List<Object[]> countOrdersByCustomer();
}

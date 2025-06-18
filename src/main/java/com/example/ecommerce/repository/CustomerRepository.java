package com.example.ecommerce.repository;

import com.example.ecommerce.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query(value = "SELECT * FROM CUSTOMERS WHERE email = :email", nativeQuery = true)
    Optional<Customer> findByEmail(@Param("email") String email);

    @Query(value = "SELECT * FROM CUSTOMERS WHERE city = :city", nativeQuery = true)
    List<Customer> findByCity(@Param("city") String city);

    @Query(value = "SELECT c.* FROM CUSTOMERS c JOIN ORDERS o ON c.id = o.customer_id GROUP BY c.id ORDER BY COUNT(o.order_id) DESC LIMIT 10", nativeQuery = true)
    List<Customer> findTopCustomersByOrderCount();
}

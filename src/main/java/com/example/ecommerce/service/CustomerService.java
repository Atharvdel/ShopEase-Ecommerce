package com.example.ecommerce.service;

import com.example.ecommerce.model.Customer;
import java.util.List;
import java.util.Optional;

public interface CustomerService {
    Customer saveCustomer(Customer customer);
    List<Customer> getAllCustomers();
    Optional<Customer> getCustomerById(Long id);
    Optional<Customer> getCustomerByEmail(String email);
    List<Customer> getCustomersByCity(String city);
    List<Customer> getTopCustomers();
    void deleteCustomer(Long id);
}

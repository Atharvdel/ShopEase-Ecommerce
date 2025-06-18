package com.example.ecommerce.config;

import com.example.ecommerce.model.Admin;
import com.example.ecommerce.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AdminInitializer implements CommandLineRunner {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Check if the admin already exists
        Optional<Admin> existingAdmin = adminRepository.findById(11L); // Assuming the ID is 1

        if (existingAdmin.isPresent()) {
            Admin admin = existingAdmin.get();
            // Update the password to a hashed version
            admin.setPassword(passwordEncoder.encode("123"));
            adminRepository.save(admin);
        } else {
            System.out.println("Admin not found. Please ensure the admin entry exists in the database.");
        }
    }
}

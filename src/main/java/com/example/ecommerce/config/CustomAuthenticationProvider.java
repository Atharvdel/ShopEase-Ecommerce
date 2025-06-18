package com.example.ecommerce.config;

import com.example.ecommerce.model.Admin;
import com.example.ecommerce.model.Customer;
import com.example.ecommerce.model.Seller;
import com.example.ecommerce.service.CustomerService;
import com.example.ecommerce.service.SellerService;
import com.example.ecommerce.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import java.util.Collections;
import java.util.Optional;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final CustomerService customerService;
    private final SellerService sellerService;
    private final AdminService adminService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomAuthenticationProvider(CustomerService customerService,
                                        SellerService sellerService,
                                        AdminService adminService,
                                        PasswordEncoder passwordEncoder) {
        this.customerService = customerService;
        this.sellerService = sellerService;
        this.adminService = adminService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();

        // Get the current request URI using RequestContextHolder
        String requestURI = "";
        try {
            requestURI = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                    .getRequest().getRequestURI();
            System.out.println("Request URI: " + requestURI); // Debugging log
        } catch (Exception e) {
            System.err.println("Error getting request URI: " + e.getMessage());
            // Fall back to the previous approach if this fails
            WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
            requestURI = details.toString();
        }

        if (requestURI.contains("/api/admin/login")) {
            System.out.println("Attempting admin authentication with ID: " + email); // Debugging log
            return authenticateAdmin(email, password);
        } else if (requestURI.contains("/api/seller/login")) {
            return authenticateSeller(email, password);
        } else if (requestURI.contains("/api/customer/login")) {
            return authenticateCustomer(email, password);
        } else {
            // Default to customer authentication
            try {
                return authenticateCustomer(email, password);
            } catch (BadCredentialsException e) {
                try {
                    return authenticateSeller(email, password);
                } catch (BadCredentialsException ex) {
                    throw new BadCredentialsException("Invalid email or password");
                }
            }
        }
    }


    private Authentication authenticateCustomer(String email, String password) {
        Optional<Customer> customerOpt = customerService.getCustomerByEmail(email);

        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            if (passwordEncoder.matches(password, customer.getPassword())) {
                return new UsernamePasswordAuthenticationToken(
                        email,
                        password,
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_CUSTOMER"))
                );
            }
        }

        throw new BadCredentialsException("Invalid email or password");
    }

    private Authentication authenticateSeller(String email, String password) {
        Optional<Seller> sellerOpt = sellerService.getSellerByEmail(email);

        if (sellerOpt.isPresent()) {
            Seller seller = sellerOpt.get();
            if (passwordEncoder.matches(password, seller.getPassword())) {
                return new UsernamePasswordAuthenticationToken(
                        email,
                        password,
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_SELLER"))
                );
            }
        }

        throw new BadCredentialsException("Invalid email or password");
    }

    private Authentication authenticateAdmin(String adminIdStr, String password) {
        try {
            Long adminId = Long.parseLong(adminIdStr);
            Optional<Admin> adminOpt = adminService.getAdminById(adminId);

            if (adminOpt.isPresent()) {
                Admin admin = adminOpt.get();
                if (passwordEncoder.matches(password, admin.getPassword())) {
                    return new UsernamePasswordAuthenticationToken(
                            adminIdStr,
                            password,
                            Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"))
                    );
                }
            }
        } catch (NumberFormatException e) {
            throw new BadCredentialsException("Invalid Admin ID format");
        }

        throw new BadCredentialsException("Invalid Admin ID or password");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}

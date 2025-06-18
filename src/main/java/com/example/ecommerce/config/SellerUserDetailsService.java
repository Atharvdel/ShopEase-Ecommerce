package com.example.ecommerce.config;

import com.example.ecommerce.model.Seller;
import com.example.ecommerce.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class SellerUserDetailsService implements UserDetailsService {

    private final SellerService sellerService;

    @Autowired
    public SellerUserDetailsService(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Seller seller = sellerService.getSellerByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Seller not found with email: " + email));

        return new User(
                seller.getEmail(),
                seller.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_SELLER"))
        );
    }
}

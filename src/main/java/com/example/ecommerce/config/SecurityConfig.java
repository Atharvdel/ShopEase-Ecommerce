package com.example.ecommerce.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private CustomAuthenticationProvider authenticationProvider;

    @Bean
    @Order(1) // Higher priority
    public SecurityFilterChain sellerSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher(AntPathRequestMatcher.antMatcher("/api/seller/**"))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/sellerlogin.html", "/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/api/seller/**", "/selleranalytics.html", "/sellerhomepage.html").hasRole("SELLER")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/sellerlogin.html")
                        .loginProcessingUrl("/api/seller/login")
                        .defaultSuccessUrl("/sellerhomepage.html", true)
                        .failureUrl("/sellerlogin.html?error=true")
                        .permitAll()
                )
                .authenticationProvider(authenticationProvider);
        return http.build();
    }

    @Bean
    @Order(2) // Second priority
    public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher(AntPathRequestMatcher.antMatcher("/api/admin/**"))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/adminlogin.html", "/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/api/admin/**", "adminanalytics.html", "adminselleranalytics.html","adminproductanalytics.html", "/adminseller.html").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/adminlogin.html")
                        .loginProcessingUrl("/api/admin/login")
                        .defaultSuccessUrl("/adminseller.html", true)
                        .failureUrl("/adminlogin.html?error=true")
                        .permitAll()
                )
                .authenticationProvider(authenticationProvider);
        return http.build();
    }

    @Bean
    @Order(3) // Lowest priority - catches everything else
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Public pages accessible to all users
                        .requestMatchers("/", "/ecom.html", "/css/**", "/js/**", "/images/**",
                                "/customer.html", "/customerslogin.html", "/seller.html",
                                "/sellerlogin.html", "/adminlogin.html",
                                "/api/public/**", "/Aboutus.html", "/beauty.html", "/phones.html",
                                "/books.html", "/shoes.html", "/furniture.html", "/toys.html",
                                "/appliances.html").permitAll()

                        // Customer-specific pages
                        .requestMatchers("/api/customer/**", "/postlogin.html", "/fashion.html",
                                "/product.html", "/productdetail.html", "/cart.html", "/orderconfirmation.html", "/payment.html",
                                "/aboutuslogin.html").hasRole("CUSTOMER")

                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/customerslogin.html")
                        .loginProcessingUrl("/api/customer/login")
                        .defaultSuccessUrl("/postlogin.html", true)
                        .failureUrl("/customerslogin.html?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/api/logout")
                        .logoutSuccessUrl("/ecom.html")
                        .permitAll()
                );

        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }
}

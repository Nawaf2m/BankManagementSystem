package com.example.bankmanagementsystem.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@org.springframework.context.annotation.Configuration
@EnableWebSecurity
public class Configuration {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable())

                .sessionManagement(sm ->
                        sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth

                        // Auth
                        .requestMatchers("/api/v1/auth/get").hasAuthority("ADMIN")

                        // Customer
                        .requestMatchers("/api/v1/customer/register").permitAll()
                        .requestMatchers("/api/v1/customer/get").hasAnyAuthority("ADMIN", "EMPLOYEE")
                        .requestMatchers("/api/v1/customer/update/*").hasAuthority("CUSTOMER")
                        .requestMatchers("/api/v1/customer/delete/*").hasAuthority("ADMIN")

                        // Employee
                        .requestMatchers("/api/v1/employee/register").hasAuthority("ADMIN")
                        .requestMatchers("/api/v1/employee/get").hasAuthority("ADMIN")
                        .requestMatchers("/api/v1/employee/update/*").hasAuthority("ADMIN")
                        .requestMatchers("/api/v1/employee/delete/*").hasAuthority("ADMIN")

                        // Account - Admin / Employee
                        .requestMatchers("/api/v1/account/get").hasAnyAuthority("ADMIN", "EMPLOYEE")
                        .requestMatchers("/api/v1/account/activate/*").hasAnyAuthority("ADMIN", "EMPLOYEE")
                        .requestMatchers("/api/v1/account/block/*").hasAnyAuthority("ADMIN", "EMPLOYEE")

                        // Account - Customer
                        .requestMatchers("/api/v1/account/add").hasAuthority("CUSTOMER")
                        .requestMatchers("/api/v1/account/get/my/accounts").hasAuthority("CUSTOMER")
                        .requestMatchers("/api/v1/account/get/details/*").hasAuthority("CUSTOMER")
                        .requestMatchers("/api/v1/account/update/*").hasAuthority("CUSTOMER")
                        .requestMatchers("/api/v1/account/delete/*").hasAuthority("CUSTOMER")
                        .requestMatchers("/api/v1/account/deposit/*").hasAuthority("CUSTOMER")
                        .requestMatchers("/api/v1/account/withdraw/*").hasAuthority("CUSTOMER")
                        .requestMatchers("/api/v1/account/transfer/*/*").hasAuthority("CUSTOMER")

                        .anyRequest().denyAll())

                .httpBasic(basic -> {})

                .build();
    }

}

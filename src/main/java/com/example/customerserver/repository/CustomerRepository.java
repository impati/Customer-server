package com.example.customerserver.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.customerserver.domain.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByUserId(final String userId);

    boolean existsCustomerByUsername(final String username);

    boolean existsCustomerByEmail(final String email);
}

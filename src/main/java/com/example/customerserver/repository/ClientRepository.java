package com.example.customerserver.repository;

import com.example.customerserver.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findClientByClientId(String clientId);
}

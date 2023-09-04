package com.example.customerserver.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.customerserver.domain.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findClientByClientId(final String clientId);
}

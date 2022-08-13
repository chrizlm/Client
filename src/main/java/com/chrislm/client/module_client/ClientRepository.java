package com.chrislm.client.module_client;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    @Query("" +
    "SELECT CASE WHEN COUNT(s) > 0 THEN " +
    "TRUE ELSE FALSE END " +
    "FROM Client s " +
    "WHERE s.email = ?1")
    Boolean existsByEmail(String email);
    Optional<Client> findByEmail(String email);
}

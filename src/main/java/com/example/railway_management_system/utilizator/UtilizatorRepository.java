package com.example.railway_management_system.utilizator;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UtilizatorRepository extends JpaRepository<Utilizator, Long> {
    @Query("SELECT u FROM Utilizator u WHERE u.email = ?1")
    Optional<Utilizator> findUtilizatorByEmail(String email);
}
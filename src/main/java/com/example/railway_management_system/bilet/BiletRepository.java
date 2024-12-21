package com.example.railway_management_system.bilet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BiletRepository extends JpaRepository<Bilet, Long> {
    @Query("SELECT b FROM Bilet b WHERE b.utilizator.id = ?1")
    List<Bilet> findBileteForUtilizator(Long utilizatorId);
}

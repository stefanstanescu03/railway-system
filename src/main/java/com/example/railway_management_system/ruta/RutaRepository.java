package com.example.railway_management_system.ruta;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RutaRepository extends JpaRepository<Ruta, Long> {
    @Query("SELECT r FROM Ruta r " +
            "WHERE r.statiePlecare = ?1 AND r.statieDestinatie = ?2")
    Optional<Ruta> findRutaByPlecareAndDestinatie(String plecare,
                                                  String destinatie);
}

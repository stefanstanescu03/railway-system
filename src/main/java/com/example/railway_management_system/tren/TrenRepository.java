package com.example.railway_management_system.tren;

import com.example.railway_management_system.program.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TrenRepository extends JpaRepository<Tren, Long> {
    @Query("SELECT t FROM Tren t WHERE t.denumire = ?1")
    Optional<Tren> findTrenByDenumire(String denumire);

    @Query(nativeQuery = true,
            value = "SELECT Program.program_id FROM Program " +
                    "INNER JOIN Tren ON Program.tren_id = Tren.tren_id " +
                    "WHERE Tren.denumire = ?1")
    List<Long> findProgrameByTren(String denumire);
}

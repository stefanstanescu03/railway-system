/** Interfata pentru TrenRepository
 * @author Stanescu Stefan
 * @version 10 Decembrie 2024
 */

package com.example.railway_management_system.tren;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TrenRepository extends JpaRepository<Tren, Long> {
    @Query("SELECT t FROM Tren t WHERE t.denumire = ?1")
    Optional<Tren> findTrenByDenumire(String denumire);

    @Query(nativeQuery = true,
            value = "SELECT program.program_id FROM program " +
                    "INNER JOIN tren ON program.tren_id = tren.tren_id " +
                    "WHERE tren.denumire = ?1")
    List<Long> findProgrameByTren(String denumire);
}

/** Interfata pentru BiletRepository
 * @author Stanescu Stefan
 * @version 10 Decembrie 2024
 */

package com.example.railway_management_system.bilet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BiletRepository extends JpaRepository<Bilet, Long> {
    @Query("SELECT b FROM Bilet b WHERE b.utilizator.id = ?1")
    List<Bilet> findBileteForUtilizator(Long utilizatorId);

    @Query("SELECT b FROM Bilet b WHERE b.program.id = ?1")
    List<Bilet> findBileteByProgram(Long programId);

//    @Query(nativeQuery = true,
//            value = "SELECT Bilet.loc FROM Bilet " +
//                    "INNER JOIN Program ON Bilet.program_id = Program.program_id " +
//                    "INNER JOIN Tren ON Tren.tren_id = Program.tren_id " +
//                    "WHERE Tren.tren_id = ?1")
//    List<Integer>findLocuriByTren(Long trenId);
}

/** Clasa pentru TrenService
 * @author Stanescu Stefan
 * @version 10 Decembrie 2024
 */

package com.example.railway_management_system.tren;

import com.example.railway_management_system.config.JwtService;
import com.example.railway_management_system.program.Program;
import com.example.railway_management_system.program.ProgramRepository;
import com.example.railway_management_system.utilizator.Rol;
import com.example.railway_management_system.utilizator.Utilizator;
import com.example.railway_management_system.utilizator.UtilizatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TrenService {

    private final TrenRepository trenRepository;
    private final ProgramRepository programRepository;
    private final JwtService jwtService;
    private final UtilizatorRepository utilizatorRepository;

    @Autowired
    public TrenService(TrenRepository trenRepository,
                       ProgramRepository programRepository,
                       JwtService jwtService,
                       UtilizatorRepository utilizatorRepository) {
        this.trenRepository = trenRepository;
        this.programRepository = programRepository;
        this.jwtService = jwtService;
        this.utilizatorRepository = utilizatorRepository;
    }

    public List<Tren> getTrenuri(String authHeader) {
        if (!isAdmin(authHeader)) {
            throw new IllegalStateException("nu aveti acces la aceasta ruta");
        }
        return trenRepository.findAll();
    }

    public Tren getTren(Long trenId, String authHeader) {
        if (!isAdmin(authHeader)) {
            throw new IllegalStateException("nu aveti acces la aceasta ruta");
        }
        return trenRepository.findById(trenId).orElseThrow(() ->
                new IllegalStateException("trenul cu id " +
                        trenId + " nu exista"));
    }

    public Tren getTrenDenumire(String denumire, String authHeader) {
        if (!isAdmin(authHeader)) {
            throw new IllegalStateException("nu aveti acces la aceasta ruta");
        }
        Optional<Tren> tren = trenRepository.findTrenByDenumire(denumire);
        if (tren.isEmpty()) {
            throw new IllegalStateException("Trenul cu denumirea "
                    + denumire + " nu exista");
        }
        return tren.get();
    }

    public void inregistrareTren(Tren tren, String authHeader) {
        if (!isAdmin(authHeader)) {
            throw new IllegalStateException("nu aveti acces la aceasta ruta");
        }
        Optional<Tren> trenExistent = trenRepository
                .findTrenByDenumire(tren.getDenumire());
        if (trenExistent.isPresent()) {
            throw new IllegalStateException("Trenul cu denumirea "
                    + tren.getDenumire() + " deja exista");
        }

        if (tren.getDenumire() == null) {
            throw new IllegalStateException("campul denumire nu este completat");
        }
        if (tren.getCapacitate() == null) {
            throw new IllegalStateException("campul capacitate nu este completat");
        }
        if (tren.getNumarVagoane() == null) {
            throw new IllegalStateException("campul numarVagoane nu este completat");
        }

        trenRepository.save(tren);
    }

    public void stergereTren(Long trenId, String authHeader) {
        if (!isAdmin(authHeader)) {
            throw new IllegalStateException("nu aveti acces la aceasta ruta");
        }
        boolean exists = trenRepository.existsById(trenId);
        if (!exists) {
            throw new IllegalStateException("trenul cu id " + trenId + " nu exista");
        }
        trenRepository.deleteById(trenId);
    }

    public void modificareTren(Long trenId, String denumire, Integer capacitate,
                               Integer numarVagoane, String authHeader) {
        if (!isAdmin(authHeader)) {
            throw new IllegalStateException("nu aveti acces la aceasta ruta");
        }
        boolean exists = trenRepository.existsById(trenId);
        if (!exists) {
            throw new IllegalStateException("trenul cu id " + trenId + " nu exista");
        }
        Tren tren = trenRepository.getReferenceById(trenId);

        if (!denumire.isEmpty()) {
            tren.setDenumire(denumire);
        }

        if (capacitate != null) {
            tren.setCapacitate(capacitate);
        }

        if (numarVagoane != null) {
            tren.setNumarVagoane(numarVagoane);
        }

        trenRepository.save(tren);
    }

    public List<Program> getProgramTren(String denumire, String authHeader) {
        if (!isAdmin(authHeader)) {
            throw new IllegalStateException("nu aveti acces la aceasta ruta");
        }
        Optional<Tren> tren = trenRepository.findTrenByDenumire(denumire);
        if (tren.isEmpty()) {
            throw new IllegalStateException("Trenul cu denumirea "
                    + denumire + " nu exista");
        }

        List<Long> programeId = trenRepository.findProgrameByTren(denumire);
        List<Program> programe = new ArrayList<Program>();

        for (Long id : programeId) {
            Program program = programRepository.findById(id).orElseThrow(() ->
                    new IllegalStateException("programul cu id "
                    + id + " nu exista")
            );
            programe.add(program);
        }

        return programe;
    }

    private boolean isAdmin(String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalStateException("no authorization");
        }

        String token = authHeader.substring(7);
        String emailExtras = jwtService.extractEmail(token);

        Optional<Utilizator> utilizator = utilizatorRepository
                .findUtilizatorByEmail(emailExtras);
        if (utilizator.isEmpty()) {
            throw new IllegalStateException("email-ul nu e inregistrat");
        }

        return utilizator.get().getRol() == Rol.ADMIN;
    }
}

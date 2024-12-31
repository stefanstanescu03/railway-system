package com.example.railway_management_system.program;

import com.example.railway_management_system.config.JwtService;
import com.example.railway_management_system.tren.Tren;
import com.example.railway_management_system.tren.TrenRepository;
import com.example.railway_management_system.tren.TrenService;
import com.example.railway_management_system.utilizator.Rol;
import com.example.railway_management_system.utilizator.Utilizator;
import com.example.railway_management_system.utilizator.UtilizatorRepository;
import com.example.railway_management_system.utilizator.UtilizatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProgramService {

    private final ProgramRepository programRepository;
    private final TrenRepository trenRepository;
    private final JwtService jwtService;
    private final UtilizatorRepository utilizatorRepository;

    @Autowired
    public ProgramService(ProgramRepository programRepository,
                          TrenRepository trenRepository,
                          JwtService jwtService,
                          UtilizatorRepository utilizatorRepository) {
        this.programRepository = programRepository;
        this.trenRepository = trenRepository;
        this.jwtService = jwtService;
        this.utilizatorRepository = utilizatorRepository;
    }

    public List<Program> getPrograme(String authHeader) {
        if (!isAdmin(authHeader)) {
            throw new IllegalStateException("nu aveti acces la aceasta ruta");
        }
        return programRepository.findAll();
    }

    public Program getProgram(Long programId) {
        return programRepository.findById(programId).orElseThrow(() ->
                new IllegalStateException("programul cu id " +
                        programId + " nu exista")
        );
    }

    public void stergereProgram(Long programId, String authHeader) {
        if (!isAdmin(authHeader)) {
            throw new IllegalStateException("nu aveti acces la aceasta ruta");
        }
        boolean exists = programRepository.existsById(programId);
        if (!exists) {
            throw new IllegalStateException("nu exista programul cu id " +
                    programId);
        }
        programRepository.deleteById(programId);
    }

    public void modificareProgram(Long programId, Date dataPlecare,
                                  Date dataAjungere, Long trenId, String authHeader) {
        if (!isAdmin(authHeader)) {
            throw new IllegalStateException("nu aveti acces la aceasta ruta");
        }
        boolean exists = programRepository.existsById(programId);
        if (!exists) {
            throw new IllegalStateException("nu exista programul cu id " +
                    programId);
        }
        exists = trenRepository.existsById(trenId);
        if (!exists) {
            throw new IllegalStateException("nu exista trenul cu id " +
                    trenId);
        }

        if (dataAjungere == null) {
            throw new IllegalStateException("data sosirii nu este specificata");
        }
        if (dataPlecare == null) {
            throw new IllegalStateException("data plecarii nu este specificata");
        }

        Program program = programRepository.getReferenceById(programId);
        Tren tren = trenRepository.getReferenceById(trenId);

        program.setDataAjungere(dataAjungere);
        program.setDataPlecare(dataPlecare);
        program.setTren(tren);

        programRepository.save(program);
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

package com.example.railway_management_system.ruta;

import com.example.railway_management_system.config.JwtService;
import com.example.railway_management_system.program.Program;
import com.example.railway_management_system.program.ProgramRepository;
import com.example.railway_management_system.tren.Tren;
import com.example.railway_management_system.tren.TrenRepository;
import com.example.railway_management_system.utilizator.Rol;
import com.example.railway_management_system.utilizator.Utilizator;
import com.example.railway_management_system.utilizator.UtilizatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RutaService {

     private final RutaRepository rutaRepository;
     private final ProgramRepository programRepository;
     private final TrenRepository trenRepository;
     private final JwtService jwtService;
     private final UtilizatorRepository utilizatorRepository;

     @Autowired
     public RutaService(RutaRepository rutaRepository,
                        ProgramRepository programRepository,
                        TrenRepository trenRepository,
                        JwtService jwtService,
                        UtilizatorRepository utilizatorRepository) {
         this.rutaRepository = rutaRepository;
         this.programRepository = programRepository;
         this.trenRepository = trenRepository;
         this.jwtService = jwtService;
         this.utilizatorRepository = utilizatorRepository;
     }

     public List<Ruta> getRute(String authHeader) {
         if (!isAdmin(authHeader)) {
             throw new IllegalStateException("nu aveti acces la aceasta ruta");
         }
         return rutaRepository.findAll();
     }

     public List<Program> getPrograme(String statiePlecare, String statieDestinatie) {
         Optional<Ruta> ruta = rutaRepository
                 .findRutaByPlecareAndDestinatie(statiePlecare,
                         statieDestinatie);
         if (ruta.isEmpty()) {
             throw new IllegalStateException("ruta nu este existenta");
         }
         return programRepository.findProgrameForRuta(ruta.get().getRutaId());
     }

     public List<String> getStatii(String statiePlecare, String statieDestinatie) {
         List<Ruta> rute = rutaRepository.findAll();
         Set<String> statiiSet = new HashSet<>();

         for (Ruta ruta : rute) {
             statiiSet.add(ruta.getStatiePlecare());
             statiiSet.add(ruta.getStatieDestinatie());
         }
         List<String> statii = new ArrayList<String>(statiiSet);

         Graph graph = new Graph(statii.size());
         for (Ruta ruta : rute) {
             graph.addEdge(statii.indexOf(ruta.getStatiePlecare()),
                     statii.indexOf(ruta.getStatieDestinatie()), ruta.getDurata());
         }
         List<Integer> shortest = graph.shortestPath(statii.indexOf(statiePlecare),
                 statii.indexOf(statieDestinatie));

         List<String> statiiFinal = new ArrayList<String>();
         for (Integer i : shortest) {
             statiiFinal.add(statii.get(i));
         }
         return statiiFinal.reversed();
     }

     public void inregistrareRuta(Ruta ruta, String authHeader) {
         if (!isAdmin(authHeader)) {
             throw new IllegalStateException("nu aveti acces la aceasta ruta");
         }
         Optional<Ruta> rutaExistenta = rutaRepository
                 .findRutaByPlecareAndDestinatie(ruta.getStatiePlecare(),
                         ruta.getStatieDestinatie());
         if (rutaExistenta.isPresent()) {
             throw new IllegalStateException("ruta deja existenta");
         }

         if (Objects.equals(ruta.getDistanta(), null)) {
             throw new IllegalStateException("campul distanta nu este completat");
         }
         if (Objects.equals(ruta.getDurata(), null)) {
             throw new IllegalStateException("campul durata nu este completat");
         }
         if (Objects.equals(ruta.getStatiePlecare(), null)) {
             throw new IllegalStateException("campul statiePlecare nu este completat");
         }
         if (Objects.equals(ruta.getStatieDestinatie(), null)) {
             throw new IllegalStateException("campul statieDestinatie nu este completat");
         }

         rutaRepository.save(ruta);
     }

     public void stergereRuta(Long rutaId, String authHeader) {
         if (!isAdmin(authHeader)) {
             throw new IllegalStateException("nu aveti acces la aceasta ruta");
         }
         boolean exists = rutaRepository.existsById(rutaId);
         if (!exists) {
             throw new IllegalStateException("ruta cu id " + rutaId +
                     " nu existenta");
         }
         rutaRepository.deleteById(rutaId);
     }

     public void modificareRuta(Long rutaId, String statiePlecare,
                                String statieDestinatie, Integer distanta,
                                Integer durata, String authHeader) {
         if (!isAdmin(authHeader)) {
             throw new IllegalStateException("nu aveti acces la aceasta ruta");
         }
         boolean exists = rutaRepository.existsById(rutaId);
         if (!exists) {
             throw new IllegalStateException("ruta cu id " + rutaId +
                     " nu existenta");
         }
         Ruta rutaToUpdate = rutaRepository.getReferenceById(rutaId);

         if (statiePlecare != null && !statiePlecare.isEmpty()) {
             rutaToUpdate.setStatiePlecare(statiePlecare);
         }
         if (statieDestinatie != null && !statieDestinatie.isEmpty()) {
             rutaToUpdate.setStatieDestinatie(statieDestinatie);
         }
         if (durata != null) {
             rutaToUpdate.setDurata(durata);
         }
         if (distanta != null) {
             rutaToUpdate.setDistanta(distanta);
         }
         rutaRepository.save(rutaToUpdate);
     }

     public void inregistrareProgram(Long rutaId, Long trenId, Program program,
                                     String authHeader) {
         if (!isAdmin(authHeader)) {
             throw new IllegalStateException("nu aveti acces la aceasta ruta");
         }
         boolean exists = rutaRepository.existsById(rutaId);
         if (!exists) {
             throw new IllegalStateException("ruta cu id " + rutaId +
                     " nu existenta");
         }
         exists = trenRepository.existsById(trenId);
         if (!exists) {
             throw new IllegalStateException("trenul cu id " + trenId + " nu exista");
         }
         if (program.getDataPlecare() == null) {
             throw new IllegalStateException("nu este specificata data plecarii");
         }
         if (program.getDataAjungere() == null) {
             throw new IllegalStateException("nu este specificata data sosirii");
         }

         Ruta ruta = rutaRepository.getReferenceById(rutaId);
         Tren tren = trenRepository.getReferenceById(trenId);

         program.setRuta(ruta);
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

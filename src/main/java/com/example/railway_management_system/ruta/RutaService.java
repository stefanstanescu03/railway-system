package com.example.railway_management_system.ruta;

import com.example.railway_management_system.program.Program;
import com.example.railway_management_system.program.ProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class RutaService {

     private final RutaRepository rutaRepository;
     private final ProgramRepository programRepository;

     @Autowired
     public RutaService(RutaRepository rutaRepository,
                        ProgramRepository programRepository) {
         this.rutaRepository = rutaRepository;
         this.programRepository = programRepository;
     }

     public List<Ruta> getRute() {
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

     public void inregistrareRuta(Ruta ruta) {
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

     public void stergereRuta(Long rutaId) {
         boolean exists = rutaRepository.existsById(rutaId);
         if (!exists) {
             throw new IllegalStateException("ruta cu id " + rutaId +
                     " nu existenta");
         }
         rutaRepository.deleteById(rutaId);
     }

     public void modificareRuta(Long rutaId, String statiePlecare,
                                String statieDestinatie, Integer distanta,
                                Integer durata) {
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

     public void inregistrareProgram(Long rutaId, Program program) {
         boolean exists = rutaRepository.existsById(rutaId);
         if (!exists) {
             throw new IllegalStateException("ruta cu id " + rutaId +
                     " nu existenta");
         }
         if (program.getDataPlecare() == null) {
             throw new IllegalStateException("nu este specificata data plecarii");
         }
         if (program.getDataAjungere() == null) {
             throw new IllegalStateException("nu este specificata data sosirii");
         }

         Ruta ruta = rutaRepository.getReferenceById(rutaId);

         program.setRuta(ruta);
         programRepository.save(program);
     }
}

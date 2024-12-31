package com.example.railway_management_system.bilet;

import com.example.railway_management_system.config.JwtService;
import com.example.railway_management_system.tren.Tren;
import com.example.railway_management_system.utilizator.Utilizator;
import com.example.railway_management_system.utilizator.UtilizatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class BiletService {

    private final BiletRepository biletRepository;
    private final UtilizatorRepository utilizatorRepository;
    private final JwtService jwtService;

    @Autowired
    public BiletService(BiletRepository biletRepository,
                        JwtService jwtService,
                        UtilizatorRepository utilizatorRepository) {
        this.biletRepository = biletRepository;
        this.jwtService = jwtService;
        this.utilizatorRepository = utilizatorRepository;
    }

    public Bilet getBilet(Long biletId, Long utilizatorId, String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalStateException("no authorization");
        }

        boolean exists = biletRepository.existsById(biletId);
        if (!exists) {
            throw new IllegalStateException("biletul cu id " + biletId + " nu exista");
        }

        String token = authHeader.substring(7);
        String emailExtras = jwtService.extractEmail(token);

        Utilizator utilizator = utilizatorRepository.getReferenceById(utilizatorId);
        if (!Objects.equals(emailExtras, utilizator.getEmail())) {
            throw new IllegalStateException("nu aveti acces sa vizualizati acest bilet");
        }

        return biletRepository.findById(biletId).orElseThrow(() ->
                new IllegalStateException("biletul cu id " +
                        biletId + " nu exista")
        );

    }

    public List<Bilet> getBilete(Long utilizatorId, String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalStateException("no authorization");
        }

        boolean exists = utilizatorRepository.existsById(utilizatorId);
        if (!exists) {
            throw new IllegalStateException("utilizatorul cu id " +
                    utilizatorId + " nu exista");
        }

        String token = authHeader.substring(7);
        String emailExtras = jwtService.extractEmail(token);

        Utilizator utilizator = utilizatorRepository.getReferenceById(utilizatorId);
        if (!Objects.equals(emailExtras, utilizator.getEmail())) {
            throw new IllegalStateException("nu aveti acces sa vizualizati acest cont");
        }

        return biletRepository.findBileteForUtilizator(utilizatorId);
    }

    public void stergereBilet(Long biletId, Long utilizatorId, String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalStateException("no authorization");
        }

        boolean exists = biletRepository.existsById(biletId);
        if (!exists) {
            throw new IllegalStateException("biletul uc id " + biletId + " nu exista");
        }

        String token = authHeader.substring(7);
        String emailExtras = jwtService.extractEmail(token);

        Utilizator utilizator = utilizatorRepository.getReferenceById(utilizatorId);
        if (!Objects.equals(emailExtras, utilizator.getEmail())) {
            throw new IllegalStateException("nu aveti acces sa stergeti acest bilet");
        }

        biletRepository.deleteById(biletId);
    }

    public void modificareBilet(Long biletId, Integer loc, Integer vagon,
                                Integer clasa, Double pret, Long utilizatorId, String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalStateException("no authorization");
        }

        boolean exists = biletRepository.existsById(biletId);
        if (!exists) {
            throw new IllegalStateException("biletul uc id " + biletId
                    + " nu exista");
        }

        String token = authHeader.substring(7);
        String emailExtras = jwtService.extractEmail(token);

        Utilizator utilizator = utilizatorRepository.getReferenceById(utilizatorId);
        if (!Objects.equals(emailExtras, utilizator.getEmail())) {
            throw new IllegalStateException("nu aveti acces sa modificati acest bilet");
        }

        Bilet bilet = biletRepository.getReferenceById(biletId);
        Tren tren = bilet.getProgram().getTren();

        if (loc != null) {
            List<Integer> locuriTren = biletRepository.findLocuriByTren(tren.getTrenId());
            System.out.println(locuriTren);

            if (locuriTren.contains(loc)) {
                throw new IllegalStateException("locul selectat este deja ocupat");
            }

            if (tren.getCapacitate() < loc) {
                throw new IllegalStateException("locul selectat nu este disponibil");
            }

            bilet.setLoc(loc);
        }

        if (vagon != null) {
            if (vagon <= 0 || vagon > tren.getNumarVagoane()) {
                throw new IllegalStateException("numarul vagonului nu corespunde cu " +
                        "numarul de vagoane ale trenului");
            }
            bilet.setVagon(vagon);
        }

        if (pret != null) {
            bilet.setPret(pret);
        }

        if (clasa != null) {
            if (clasa > 2 || clasa < 1) {
                throw new IllegalStateException("clasa selectata trebuie sa fie 1 sau 2");
            }
            bilet.setClasa(clasa);
        }

        biletRepository.save(bilet);
    }
}

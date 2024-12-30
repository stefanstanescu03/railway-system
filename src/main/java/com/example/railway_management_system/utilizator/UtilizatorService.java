package com.example.railway_management_system.utilizator;

import com.example.railway_management_system.bilet.Bilet;
import com.example.railway_management_system.bilet.BiletRepository;
import com.example.railway_management_system.config.JwtService;
import com.example.railway_management_system.program.Program;
import com.example.railway_management_system.program.ProgramRepository;
import com.example.railway_management_system.tren.Tren;
import com.example.railway_management_system.tren.TrenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UtilizatorService {

    private final UtilizatorRepository utilizatorRepository;
    private final BiletRepository biletRepository;
    private final ProgramRepository programRepository;
    private final TrenRepository trenRepository;

    private final JwtService jwtService;

    @Autowired
    public UtilizatorService(UtilizatorRepository utilizatorRepository,
                             BiletRepository biletRepository,
                             ProgramRepository programRepository,
                             TrenRepository trenRepository,
                             JwtService jwtService) {
        this.utilizatorRepository = utilizatorRepository;
        this.biletRepository = biletRepository;
        this.programRepository = programRepository;
        this.trenRepository = trenRepository;
        this.jwtService = jwtService;
    }

    public Utilizator getUtilizator(Long utilizatorId, String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalStateException("not authorization");
        }

        String token = authHeader.substring(7);
        String emailExtras = jwtService.extractEmail(token);

        Optional<Utilizator> utilizator = utilizatorRepository
                .findUtilizatorByEmail(emailExtras);

        if (utilizator.isPresent()) {
            if (!Objects.equals(utilizator.get().getUtilizatorId(), utilizatorId)) {
                throw new IllegalStateException("nu aveti acces sa vizualizati acest cont");
            }
        }

        return utilizatorRepository.findById(utilizatorId).orElseThrow(() ->
                new IllegalStateException("utilizatorul cu id " +
                        utilizatorId + " nu exista")
        );

    }

    public Utilizator getUtilizatorByEmail(String email, String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalStateException("not authorization");
        }

        String token = authHeader.substring(7);
        String emailExtras = jwtService.extractEmail(token);

        if (!Objects.equals(email, emailExtras)) {
            throw new IllegalStateException("nu aveti acces sa vizualizati acest cont");
        }

        return utilizatorRepository.findUtilizatorByEmail(email).orElseThrow(() ->
                new IllegalStateException("utilizatorul cu email " +
                        email + " nu exista")
        );
    }

    public void stergereUtilizator(Long utilizatorId, String authHeader) {
        boolean exists = utilizatorRepository.existsById(utilizatorId);
        if (!exists) {
            throw new IllegalStateException("utilizatorul cu id " +
                    utilizatorId + " nu exista");
        }

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalStateException("not authorization");
        }

        String token = authHeader.substring(7);
        String emailExtras = jwtService.extractEmail(token);

        Utilizator utilizator = utilizatorRepository.getReferenceById(utilizatorId);
        if (!Objects.equals(emailExtras, utilizator.getEmail())) {
            throw new IllegalStateException("nu aveti acces sa stergeti acest cont");
        }

        utilizatorRepository.deleteById(utilizatorId);
    }

    public void modificareUtilizator(Long utilizatorId, String nume, String prenume,
                              String email, String telefon, String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalStateException("not authorization");
        }

        String token = authHeader.substring(7);
        String emailExtras = jwtService.extractEmail(token);

        boolean exists = utilizatorRepository.existsById(utilizatorId);
        if (!exists) {
            throw new IllegalStateException("utilizatorul cu id " + utilizatorId + " nu exista");
        }

        Optional<Utilizator> utilizatorExistent = utilizatorRepository.findUtilizatorByEmail(email);
        if (utilizatorExistent.isPresent() && !Objects.equals(email, emailExtras)) {
            throw new IllegalStateException("email deja inregistrat");
        }

        Utilizator utilizatorToUpdate = utilizatorRepository.getReferenceById(utilizatorId);

        if (!Objects.equals(emailExtras, utilizatorToUpdate.getEmail())) {
            throw new IllegalStateException("nu aveti acces sa modificati acest cont");
        }

        if (nume != null && !nume.isEmpty()) {
            utilizatorToUpdate.setNume(nume);
        }
        if (prenume != null && !prenume.isEmpty()) {
            utilizatorToUpdate.setPrenume(prenume);
        }
        if (email != null && !email.isEmpty()) {
            verificareEmail(email);
            utilizatorToUpdate.setEmail(email);
        }
        if (telefon != null && !telefon.isEmpty()) {
            verificareTelefon(telefon);
            utilizatorToUpdate.setTelefon(telefon);
        }

        utilizatorRepository.save(utilizatorToUpdate);
    }

    public void inregistrareBilet(Long utilizatorId, Long programId, Bilet bilet,
                                  String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalStateException("not authorization");
        }

        String token = authHeader.substring(7);
        String emailExtras = jwtService.extractEmail(token);

        boolean exists = utilizatorRepository.existsById(utilizatorId);
        if (!exists) {
            throw new IllegalStateException("utilizatorul cu id " + utilizatorId + " nu exista");
        }
        exists = programRepository.existsById(programId);
        if (!exists) {
            throw new IllegalStateException("programul asociat nu exista");
        }

        if (bilet.getLoc() == null) {
            throw new IllegalStateException("campul loc nu este completat");
        }
        if (bilet.getPret() == null) {
            throw new IllegalStateException("campul pret nu este completat");
        }
        if (bilet.getVagon() == null) {
            throw new IllegalStateException("campul vagon nu este completat");
        }
        if (bilet.getClasa() == null) {
            throw new IllegalStateException("campul clasa nu este completat");
        }

        Program program = programRepository.getReferenceById(programId);
        Utilizator utilizator = utilizatorRepository.getReferenceById(utilizatorId);

        if (!Objects.equals(emailExtras, utilizator.getEmail())) {
            throw new IllegalStateException("nu puteti cumpara bilet pentru alt utilizator");
        }

        Tren tren = program.getTren();
        if (bilet.getVagon() <= 0 || bilet.getVagon() > tren.getNumarVagoane()) {
            throw new IllegalStateException("numarul vagonului nu corespunde cu " +
                    "numarul de vagoane ale trenului");
        }

        List<Integer> locuriTren = biletRepository.findLocuriByTren(tren.getTrenId());

        if (locuriTren.size() == tren.getCapacitate()) {
            throw new IllegalStateException("trenul nu mai are locuri disponibile");
        }

        if (locuriTren.contains(bilet.getLoc())) {
            throw new IllegalStateException("locul selectat este deja ocupat");
        }

        if (tren.getCapacitate() < bilet.getLoc()) {
            throw new IllegalStateException("locul selectat nu este disponibil");
        }

        if (bilet.getClasa() > 2 || bilet.getClasa() < 1) {
            throw new IllegalStateException("clasa selectata trebuie sa fie 1 sau 2");
        }

        bilet.setUtilizator(utilizator);
        bilet.setProgram(program);
        biletRepository.save(bilet);
    }

    public void aignareTren(Long utilizatorId, Long trenId, String authHeader) {
        if (!isAdmin(authHeader)) {
            throw new IllegalStateException("nu aveti acces la aceasta ruta");
        }
        boolean exists = utilizatorRepository.existsById(utilizatorId);
        if (!exists) {
            throw new IllegalStateException("utilizatorul cu id " + utilizatorId + " nu exista");
        }
        exists = trenRepository.existsById(trenId);
        if (!exists) {
            throw new IllegalStateException("trenul cu id " + trenId + " nu exista");
        }

        Utilizator utilizator = utilizatorRepository.getReferenceById(utilizatorId);
        Tren tren = trenRepository.getReferenceById(trenId);

        if (!Objects.equals(utilizator.getRol(), Rol.PERSONAL)) {
            throw new IllegalStateException("utilizatorul nu face parte din personal");
        }

        utilizator.setTren(tren);
        utilizatorRepository.save(utilizator);
    }

    private void verificareEmail(String email) {
        if (email == null) {
            throw new IllegalStateException("email invalid");
        }
        int at = email.indexOf("@");
        if (at < 0) {
            throw new IllegalStateException("formatul email-ului este gresit");
        }
        int dot = email.lastIndexOf(".");
        if (at >= dot) {
            throw new IllegalStateException("formatul email-ului este gresit");
        }
    }

    private void verificareTelefon(String telefon) {
        if (telefon.length() != 10) {
            throw new IllegalStateException("Numarul de telefon trebuie sa contine 10 cifre");
        }
        for (Byte chr : telefon.getBytes()) {
            if ((chr >= 65 && chr <= 90) || (chr >= 97 && chr <= 122)) {
                throw new IllegalStateException("Numarul de telefon nu trebuie sa contina litere");
            }
        }
        if (!telefon.startsWith("07")) {
            throw new IllegalStateException("Numarul de telefon trebuie sa inceapa cu 07");
        }
    }

    private void verificareParola(String parola) {
        if (parola.length() < 8) {
            throw new IllegalStateException("Parola trebuie sa contina macar 8 caractere");
        }

        int numUpper = 0;
        int numLower = 0;
        int numDigits = 0;
        int numSpecial = 0;

        for (int i = 0; i < parola.length(); i++) {
            char c = parola.charAt(i);
            if (Character.isLowerCase(c)) {
                numLower = 1;
            } else
            if (Character.isUpperCase(c)) {
                numUpper = 1;
            } else
            if (Character.isDigit(c)) {
                numDigits = 1;
            } else {
                numSpecial = 1;
            }
        }

        if (numUpper == 0) {
            throw new IllegalStateException("Parola trebuie sa contina macar o litera mare");
        }
        if (numLower == 0) {
            throw new IllegalStateException("Parola trebuie sa contina macar o litera mica");
        }
        if (numDigits == 0) {
            throw new IllegalStateException("Parola trebuie sa contina macar o cifra");
        }
        if (numSpecial == 0) {
            throw new IllegalStateException("Parola trebuie sa contina macar un caracter special");
        }
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

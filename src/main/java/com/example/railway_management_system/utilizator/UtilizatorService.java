package com.example.railway_management_system.utilizator;

import com.example.railway_management_system.bilet.Bilet;
import com.example.railway_management_system.bilet.BiletRepository;
import com.example.railway_management_system.program.Program;
import com.example.railway_management_system.program.ProgramRepository;
import com.example.railway_management_system.ruta.RutaRepository;
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

    @Autowired
    public UtilizatorService(UtilizatorRepository utilizatorRepository,
                             BiletRepository biletRepository,
                             ProgramRepository programRepository) {
        this.utilizatorRepository = utilizatorRepository;
        this.biletRepository = biletRepository;
        this.programRepository = programRepository;
    }

    public List<Utilizator> getUtilizatori() {
        return utilizatorRepository.findAll();
    }

    public void inregistrareUtilizator(Utilizator utilizator) {
        Optional<Utilizator> accountByEmail = utilizatorRepository
                .findUtilizatorByEmail(utilizator.getEmail());
        if (accountByEmail.isPresent()) {
            throw new IllegalStateException("email deja inregistrat");
        }

        if (Objects.equals(utilizator.getNume(), null)) {
            throw new IllegalStateException("campul nume nu este completat");
        }

        if (Objects.equals(utilizator.getPrenume(), null)) {
            throw new IllegalStateException("campul prenume nu este completa");
        }

        if (Objects.equals(utilizator.getEmail(), null)) {
            throw new IllegalStateException("campul email nu este completat");
        }

        if (Objects.equals(utilizator.getTelefon(), null)) {
            throw new IllegalStateException("campul telefon nu este completat");
        }

        if (Objects.equals(utilizator.getParola(), null)) {
            throw new IllegalStateException("campul parola nu este completat");
        }

        verificareEmail(utilizator.getEmail());
        verificareTelefon(utilizator.getTelefon());
        verificareParola(utilizator.getParola());

        utilizatorRepository.save(utilizator);
    }

    public void stergereUtilizator(Long utilizatorId) {
        boolean exists = utilizatorRepository.existsById(utilizatorId);
        if (!exists) {
            throw new IllegalStateException("utilizatorul cu id " + utilizatorId + " nu exista");
        }
        utilizatorRepository.deleteById(utilizatorId);
    }

    public void modificareUtilizator(Long utilizatorId, String nume, String prenume,
                              String email, String telefon) {
        boolean exists = utilizatorRepository.existsById(utilizatorId);
        if (!exists) {
            throw new IllegalStateException("utilizatorul cu id " + utilizatorId + " nu exista");
        }
        Utilizator utilizatorToUpdate = utilizatorRepository.getReferenceById(utilizatorId);

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

    public void inregistrareBilet(Long utilizatorId, Long programId, Bilet bilet) {
        boolean exists = utilizatorRepository.existsById(utilizatorId);
        if (!exists) {
            throw new IllegalStateException("utilizatorul cu id " + utilizatorId + " nu exista");
        }
        exists = programRepository.existsById(programId);
        if (!exists) {
            throw new IllegalStateException("programul asociat nu exista");
        }
        Program program = programRepository.getReferenceById(programId);
        Utilizator utilizator = utilizatorRepository.getReferenceById(utilizatorId);

        bilet.setUtilizator(utilizator);
        bilet.setProgram(program);
        biletRepository.save(bilet);
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
}

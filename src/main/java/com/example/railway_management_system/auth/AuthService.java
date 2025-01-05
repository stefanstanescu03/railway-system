/** Clasa pentru AuthService
 * @author Stanescu Stefan
 * @version 10 Decembrie 2024
 */

package com.example.railway_management_system.auth;

import com.example.railway_management_system.config.JwtService;
import com.example.railway_management_system.utilizator.Rol;
import com.example.railway_management_system.utilizator.Utilizator;
import com.example.railway_management_system.utilizator.UtilizatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class AuthService {

    private final UtilizatorRepository utilizatorRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthService(UtilizatorRepository utilizatorRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager) {
        this.utilizatorRepository = utilizatorRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse register(RegisterRequest request) {

        Optional<Utilizator> accountByEmail = utilizatorRepository
                .findUtilizatorByEmail(request.getEmail());
        if (accountByEmail.isPresent()) {
            throw new IllegalStateException("email deja inregistrat");
        }

        if (request.getNume() == null || Objects.equals(request.getNume(), "")) {
            throw new IllegalStateException("campul nume nu este completat");
        }
        if (request.getPrenume() == null || Objects.equals(request.getPrenume(), "")) {
            throw new IllegalStateException("campul prenume nu este completat");
        }
        if (request.getEmail() == null || Objects.equals(request.getEmail(), "")) {
            throw new IllegalStateException("campul email nu este completat");
        }
        if (request.getTelefon() == null || Objects.equals(request.getTelefon(), "")) {
            throw new IllegalStateException("campul telefon nu este completat");
        }
        if (request.getParola() == null || Objects.equals(request.getParola(), "")) {
            throw new IllegalStateException("campul parola nu este completat");
        }
        if (request.getRol() == null) {
            throw new IllegalStateException("campul rol nu este completat");
        }

        Rol rolSelectat = switch (request.getRol()) {
            case "ADMIN" -> Rol.ADMIN;
            case "PERSONAL" -> Rol.PERSONAL;
            default -> Rol.PASAGER;
        };


        verificareEmail(request.getEmail());
        verificareTelefon(request.getTelefon());
        verificareParola(request.getParola());

        Utilizator utilizator = new Utilizator(request.getNume(), request.getPrenume(),
                request.getEmail(), request.getTelefon(),
                passwordEncoder.encode(request.getParola()), rolSelectat);

        utilizatorRepository.save(utilizator);
        String token = jwtService.generateToken(utilizator);
        return new AuthResponse(token);
    }

    public AuthResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(request.getEmail(), request.getParola())
        );
        Utilizator utilizator = utilizatorRepository.findByEmail(request.getEmail());
        String token = jwtService.generateToken(utilizator);
        return new AuthResponse(token);
    }

    private void verificareEmail(String email) {
        if (email == null) {
            throw new IllegalStateException("email invalid");
        }
        int at = email.indexOf("@");
        if (at < 0) {
            throw new IllegalStateException("formatul email-ului este gresit: nu contine @");
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
            } else if (Character.isUpperCase(c)) {
                numUpper = 1;
            } else if (Character.isDigit(c)) {
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

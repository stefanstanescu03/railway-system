package com.example.railway_management_system.utilizator;

import com.example.railway_management_system.bilet.Bilet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "api/utilizator")
public class UtilizatorController {

    private final UtilizatorService utilizatorService;

    @Autowired
    public UtilizatorController(UtilizatorService utilizatorService) {
        this.utilizatorService = utilizatorService;
    }

    @GetMapping(path = "id={utilizatorId}")
    public Utilizator getUtilizator(@PathVariable("utilizatorId") Long utilizatorId,
                                    @RequestHeader("Authorization") String authHeader) {
        return utilizatorService.getUtilizator(utilizatorId, authHeader);
    }

    @DeleteMapping(path = "id={utilizatorId}")
    public void stergereUtilizator(@PathVariable("utilizatorId") Long utilizatorId,
                                   @RequestHeader("Authorization") String authHeader) {
        utilizatorService.stergereUtilizator(utilizatorId, authHeader);
    }

    @PutMapping(
            path = "id={utilizatorId}&nume={nume}&prenume={prenume}" +
                    "&email={email}&telefon={telefon}")
    public void modificareUtilizator(@PathVariable("utilizatorId") Long utilizatorId,
                                     @PathVariable("nume") String nume,
                                     @PathVariable("prenume") String prenume,
                                     @PathVariable("email") String email,
                                     @PathVariable("telefon") String telefon,
                                     @RequestHeader("Authorization") String authHeader) {
         utilizatorService.modificareUtilizator(utilizatorId, nume, prenume, email, telefon, authHeader);
    }

    @PostMapping(path = "bilet/id={utilizatorId}&program={programId}")
    public void inregistrareBilet(@PathVariable("utilizatorId") Long utilizatorId,
                                  @PathVariable("programId") Long programId,
                                  @RequestBody Bilet bilet,
                                  @RequestHeader("Authorization") String authHeader) {
        utilizatorService.inregistrareBilet(utilizatorId, programId, bilet, authHeader);
    }

    @PostMapping(path = "tren/id={utilizatorId}&tren={trenId}")
    public void asignareTren(@PathVariable("utilizatorId") Long utilizatorId,
                             @PathVariable("trenId") Long trenId,
                             @RequestHeader("Authorization") String authHeader) {
        utilizatorService.aignareTren(utilizatorId, trenId, authHeader);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Object> handleIllegalStateException(IllegalStateException ex) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("error", "IllegalStateException");
        responseBody.put("message", ex.getMessage());
        responseBody.put("timestamp", System.currentTimeMillis());

        return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
    }
}

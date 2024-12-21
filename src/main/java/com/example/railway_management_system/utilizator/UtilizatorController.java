package com.example.railway_management_system.utilizator;

import com.example.railway_management_system.bilet.Bilet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/utilizator")
public class UtilizatorController {

    private final UtilizatorService utilizatorService;

    @Autowired
    public UtilizatorController(UtilizatorService utilizatorService) {
        this.utilizatorService = utilizatorService;
    }

    // debugging purposes this should not exist!
    @GetMapping
    public List<Utilizator> getUtilizatori() {
        return utilizatorService.getUtilizatori();
    }

    @PostMapping
    public void inregistrareUtilizator(@RequestBody Utilizator utilizator) {
        utilizatorService.inregistrareUtilizator(utilizator);
    }

    @DeleteMapping(path = "{utilizatorId}")
    public void stergereUtilizator(@PathVariable("utilizatorId") Long utilizatorId) {
        utilizatorService.stergereUtilizator(utilizatorId);
    }

    @PutMapping(
            path = "id={utilizatorId}&nume={nume}&prenume={prenume}" +
                    "&email={email}&telefon={telefon}")
    public void modificareUtilizator(@PathVariable("utilizatorId") Long utilizatorId,
                              @PathVariable("nume") String nume,
                              @PathVariable("prenume") String prenume,
                              @PathVariable("email") String email,
                              @PathVariable("telefon") String telefon) {
         utilizatorService.modificareUtilizator(utilizatorId, nume, prenume, email, telefon);
    }

    @PostMapping(path = "bilet/id={utilizatorId}&program={programId}")
    public void inregistrareBilet(@PathVariable("utilizatorId") Long utilizatorId,
                          @PathVariable("programId") Long programId,
                          @RequestBody Bilet bilet) {
        utilizatorService.inregistrareBilet(utilizatorId, programId, bilet);
    }
}

package com.example.railway_management_system.bilet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/bilet")
public class BiletController {

    private final BiletService biletService;

    @Autowired
    public BiletController(BiletService biletService) {
            this.biletService = biletService;
    }

    @GetMapping(path = "cauta/id={utilizatorId}")
    public List<Bilet> getBilete(@PathVariable("utilizatorId") Long utilizatorId) {
        return biletService.getBilete(utilizatorId);
    }


    @DeleteMapping(path = "id={biletId}")
    public void stergereBilet(@PathVariable("biletId") Long biletId) {
        biletService.stergereBilet(biletId);
    }

}

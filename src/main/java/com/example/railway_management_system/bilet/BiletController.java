package com.example.railway_management_system.bilet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "api/bilet")
public class BiletController {

    private final BiletService biletService;

    @Autowired
    public BiletController(BiletService biletService) {
            this.biletService = biletService;
    }

    @GetMapping(path = "cauta/id={utilizatorId}")
    public List<Bilet> getBilete(@PathVariable("utilizatorId") Long utilizatorId,
                                 @RequestHeader("Authorization") String authHeader) {
        return biletService.getBilete(utilizatorId, authHeader);
    }

    @DeleteMapping(path = "id={biletId}&utilizator={utilizatorId}")
    public void stergereBilet(@PathVariable("biletId") Long biletId,
                              @PathVariable("utilizatorId") Long utilizatorId,
                              @RequestHeader("Authorization") String authHeader) {
        biletService.stergereBilet(biletId, utilizatorId, authHeader);
    }

    @PutMapping(path = "id={biletId}&loc={loc}&vagon={vagon}" +
            "&clasa={clasa}&pret={pret}&utilizator={utilizatorId}")
    public void modificareBilet(@PathVariable("biletId") Long biletId,
                                @PathVariable("loc") Integer loc,
                                @PathVariable("vagon") Integer vagon,
                                @PathVariable("clasa") Integer clasa,
                                @PathVariable("pret") Integer pret,
                                @PathVariable("utilizatorId") Long utilizatorId,
                                @RequestHeader("Authorization") String authHeader) {
        biletService.modificareBilet(biletId, loc, vagon, clasa,
                pret, utilizatorId, authHeader);
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

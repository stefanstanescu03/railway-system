package com.example.railway_management_system.ruta;

import com.example.railway_management_system.program.Program;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "api/ruta")
public class RutaController {

    private final RutaService rutaService;

    @Autowired
    public RutaController(RutaService rutaService) {
        this.rutaService = rutaService;
    }

    @GetMapping
    public List<Ruta> getRute(@RequestHeader("Authorization") String authHeader) {
        return rutaService.getRute(authHeader);
    }

    @PostMapping
    public void inregistrareRuta(@RequestBody Ruta ruta,
                                 @RequestHeader("Authorization") String authHeader) {
        rutaService.inregistrareRuta(ruta, authHeader);
    }

    @DeleteMapping(path = "id={rutaId}")
    public void stergereRuta(@PathVariable("rutaId") Long rutaId,
                             @RequestHeader("Authorization") String authHeader) {
        rutaService.stergereRuta(rutaId, authHeader);
    }

    @PostMapping(path = "program/id={rutaId}&tren={trenId}")
    public void inregistrareProgram(@PathVariable("rutaId") Long rutaId,
                                    @PathVariable("trenId") Long trenId,
                                    @RequestBody Program program,
                                    @RequestHeader("Authorization") String authHeader) {
        rutaService.inregistrareProgram(rutaId, trenId,  program, authHeader);
    }

    @GetMapping(path = "cautare/plecare={statiePlecare}&" +
            "destinatie={statieDestinatie}")
    public List<Program> getPrograme(@PathVariable("statiePlecare") String statiePlecare,
                            @PathVariable("statieDestinatie") String statieDestinatie) {
        return rutaService.getPrograme(statiePlecare, statieDestinatie);
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

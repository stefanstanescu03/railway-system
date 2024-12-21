package com.example.railway_management_system.ruta;

import com.example.railway_management_system.program.Program;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/ruta")
public class RutaController {

    private final RutaService rutaService;

    @Autowired
    public RutaController(RutaService rutaService) {
        this.rutaService = rutaService;
    }

    @GetMapping
    public List<Ruta> getRute() {
        return rutaService.getRute();
    }

    @PostMapping
    public void inregistrareRuta(@RequestBody Ruta ruta) {
        rutaService.inregistrareRuta(ruta);
    }

    @DeleteMapping(path = "id={rutaId}")
    public void stergereRuta(@PathVariable("rutaId") Long rutaId) {
        rutaService.stergereRuta(rutaId);
    }

    @PostMapping(path = "program/id={rutaId}")
    public void inregistrareProgram(@PathVariable("rutaId") Long rutaId,
                               @RequestBody Program program) {
        rutaService.inregistrareProgram(rutaId, program);
    }

    @GetMapping(path = "cautare/plecare={statiePlecare}&" +
            "destinatie={statieDestinatie}")
    public List<Program> getPrograme(@PathVariable("statiePlecare") String statiePlecare,
                            @PathVariable("statieDestinatie") String statieDestinatie) {
        return rutaService.getPrograme(statiePlecare, statieDestinatie);
    }
}

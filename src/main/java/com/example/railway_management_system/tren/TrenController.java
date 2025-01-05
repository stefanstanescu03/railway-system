/** Clasa pentru TrenController
 * @author Stanescu Stefan
 * @version 10 Decembrie 2024
 */

package com.example.railway_management_system.tren;

import com.example.railway_management_system.program.Program;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "api/tren")
public class TrenController {

    private final TrenService trenService;

    @Autowired
    public TrenController(TrenService trenService) {
        this.trenService = trenService;
    }

    @GetMapping
    public List<Tren> getTrenuri(@RequestHeader("Authorization") String authHeader) {
        return trenService.getTrenuri(authHeader);
    }

    @GetMapping(path = "id={trenId}")
    public Tren getTren(@PathVariable("trenId") Long trenId,
                        @RequestHeader("Authorization") String authHeader) {
        return trenService.getTren(trenId, authHeader);
    }

    @GetMapping(path = "denumire={denumire}")
    public Tren getTrenDenumire(@PathVariable("denumire") String denumire,
                                @RequestHeader("Authorization") String authHeader) {
        return this.trenService.getTrenDenumire(denumire, authHeader);
    }

    @PostMapping
    public void inregistrareTren(@RequestBody Tren tren,
                                 @RequestHeader("Authorization") String authHeader) {
        trenService.inregistrareTren(tren, authHeader);
    }

    @DeleteMapping(path = "id={trenId}")
    public void stergereTren(@PathVariable("trenId") Long trenId,
                             @RequestHeader("Authorization") String authHeader) {
        trenService.stergereTren(trenId, authHeader);
    }

    @PutMapping(path = "id={trenId}&denumire={denumire}&capacitate={capacitate}" +
            "numarVagoane={numarVagoane}")
    public void modificareTren(@PathVariable("trenId") Long trenId,
                               @PathVariable("denumire") String denumire,
                               @PathVariable("capacitate") Integer capacitate,
                               @PathVariable("numarVagoane") Integer numarVagoane,
                               @RequestHeader("Authorization") String authHeader) {
        trenService.modificareTren(trenId, denumire, capacitate, numarVagoane, authHeader);
    }

    @GetMapping(path = "program/denumire={denumire}")
    public List<Program> getProgramTren(@PathVariable("denumire") String denumire,
                                        @RequestHeader("Authorization") String authHeader) {
        return trenService.getProgramTren(denumire, authHeader);
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

package com.example.railway_management_system.program;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "api/program")
public class ProgramController {
    private final ProgramService programService;

    @Autowired
    public ProgramController(ProgramService programService) {
        this.programService = programService;
    }

    @GetMapping
    public List<Program> getPrograme(@RequestHeader("Authorization") String authHeader) {
        return programService.getPrograme(authHeader);
    }

    @PutMapping(path = "id={programId}&dataPlecare={dataPlecare}" +
            "&dataAjungere={dataAjungere}&trenId={trenId}")
    public void modificareProgram(@PathVariable("programId") Long programId,
                                  @PathVariable("dataPlecare") Date dataPlecare,
                                  @PathVariable("dataAjungere") Date dataAjungere,
                                  @PathVariable("trenId") Long trenId,
                                  @RequestHeader("Authorization") String authHeader) {
        programService.modificareProgram(programId, dataPlecare,
                dataAjungere, trenId, authHeader);
    }

    @DeleteMapping(path = "id={programId}")
    public void stergereProgram(@PathVariable("programId") Long programId,
                                @RequestHeader("Authorization") String authHeader) {
        programService.stergereProgram(programId, authHeader);
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

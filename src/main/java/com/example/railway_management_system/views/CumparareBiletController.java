package com.example.railway_management_system.views;

import com.example.railway_management_system.bilet.Bilet;
import com.example.railway_management_system.config.JwtService;
import com.example.railway_management_system.program.Program;
import com.example.railway_management_system.utilizator.Utilizator;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Controller
public class CumparareBiletController {

    private final JwtService jwtService;

    @Autowired
    public CumparareBiletController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @GetMapping(path = "cumparare/program={id}")
    public String cumparare(@CookieValue("authToken") String token,
                            @PathVariable("id") String programId,
                            Model model) {

        getDetails(token, programId, model);

        return "cumparare";
    }

    @PostMapping(path = "cumparare/program={id}")
    public String handlePurchase(@CookieValue("authToken") String token,
                                 @PathVariable("id") String programId,
                                 @RequestParam("loc") String loc,
                                 @RequestParam("vagon") String vagon,
                                 @RequestParam("clasa") String clasa,
                                 @RequestParam("pret") String pret,
                                 Model model) {

        if (isNotNumeric(vagon)) {
            model.addAttribute("error", "vagonul trebuie sa fie " +
                    "un numar");
            getDetails(token, programId, model);
            return "cumparare";
        }

        if (isNotNumeric(loc)) {
            model.addAttribute("error", "locul trebuie sa fie " +
                    "un numar");
            getDetails(token, programId, model);
            return "cumparare";
        }

        if (isNotNumeric(pret)) {
            model.addAttribute("error", "pretul trebuie sa fie " +
                    "un numar");
            getDetails(token, programId, model);
            return "cumparare";
        }

        Bilet bilet = new Bilet(Integer.parseInt(loc),
                Integer.parseInt(vagon), Double.parseDouble(pret), Integer.parseInt(clasa));

        Long id = getId(token);

        assert id != null;
        String url = String.format("http://localhost:8080/api/utilizator/bilet/id=%s&program=%s",
                id.toString(), programId);
        System.out.println(url);

        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();

            headers.set("Authorization", "Bearer " + token);
            headers.set("Content-Type", "application/json");

            HttpEntity<Bilet> entity = new HttpEntity<>(bilet, headers);

            ResponseEntity<String> apiResponse = restTemplate
                    .exchange(url, HttpMethod.POST, entity, String.class);

            if (apiResponse.getStatusCode() == HttpStatus.OK) {
                System.out.println(apiResponse.getBody());
            }
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                String errorResponse = e.getResponseBodyAsString();
                Gson gson = new Gson();
                Map<String, Object> responseObj = gson.fromJson(errorResponse, Map.class);

                model.addAttribute("error", responseObj.get("message"));
            }
        }

        getDetails(token, programId, model);

        return "cumparare";
    }

    private void getDetails(String token, String programId, Model model) {
        String url = String.format("http://localhost:8080/api/program/id=%s", programId);
        System.out.println(url);

        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();

            headers.set("Authorization", "Bearer " + token);
            headers.set("Content-Type", "application/json");

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<Program> apiResponse = restTemplate
                    .exchange(url, HttpMethod.GET, entity, Program.class);

            if (apiResponse.getStatusCode() == HttpStatus.OK) {
                model.addAttribute("program", apiResponse.getBody());
            }

        } catch (HttpClientErrorException e) {
            System.out.println(e.getMessage());
        }
    }

    private Long getId(String token) {
        String email = jwtService.extractEmail(token);
        String url = String.format("http://localhost:8080/api/utilizator/email=%s", email);
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();

            headers.set("Authorization", "Bearer " + token);
            headers.set("Content-Type", "application/json");

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<Utilizator> apiResponse = restTemplate
                    .exchange(url, HttpMethod.GET, entity, Utilizator.class);

            if (apiResponse.getStatusCode() == HttpStatus.OK) {
                Utilizator cont = apiResponse.getBody();
                assert cont != null;
                return cont.getUtilizatorId();
            }
        } catch (HttpClientErrorException _) {

        }
        return null;
    }

    private boolean isNotNumeric(String str) {
        try {
            Double.parseDouble(str);
            return false;
        } catch(NumberFormatException e){
            return true;
        }
    }
}

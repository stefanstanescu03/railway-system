/** Clasa pentru ModificareBiletController
 * @author Stanescu Stefan
 * @version 10 Decembrie 2024
 */

package com.example.railway_management_system.views;

import com.example.railway_management_system.bilet.Bilet;
import com.example.railway_management_system.config.JwtService;
import com.example.railway_management_system.utilizator.Utilizator;
import com.google.gson.Gson;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Controller
public class ModificareBiletController {

    private final JwtService jwtService;

    @Autowired
    public ModificareBiletController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @GetMapping (path = "modificare/bilet={biletId}")
    public String modificareBilet(@CookieValue("authToken") String token,
                                  @PathVariable("biletId") String biletId,
                                  Model model,
                                  HttpServletRequest request) {

        getDetails(token, biletId, model);
        model.addAttribute("isLogged", isLogged(request));
        return "modificareBilet";
    }

    @PostMapping(path = "modificare/bilet={biletId}")
    public String handleModify(@CookieValue("authToken") String token,
                               @PathVariable("biletId") String biletId,
                               @RequestParam("loc") String loc,
                               @RequestParam("vagon") String vagon,
                               @RequestParam("clasa") String clasa,
                               @RequestParam("pret") String pret,
                               HttpServletRequest request,
                               Model model) {

        if (isNotNumeric(vagon)) {
            model.addAttribute("error", "vagonul trebuie sa fie " +
                    "un numar");
            getDetails(token, biletId, model);
            model.addAttribute("isLogged", isLogged(request));
            return "modificareBilet";
        }

        if (isNotNumeric(loc)) {
            model.addAttribute("error", "locul trebuie sa fie " +
                    "un numar");
            getDetails(token, biletId, model);
            model.addAttribute("isLogged", isLogged(request));
            return "modificareBilet";
        }

        if (isNotNumeric(pret)) {
            model.addAttribute("error", "pretul trebuie sa fie " +
                    "un numar");
            getDetails(token, biletId, model);
            model.addAttribute("isLogged", isLogged(request));
            return "modificareBilet";
        }

        Long id = getId(token);
        assert id != null;
        String url = String.format(
                "http://localhost:8080/api/bilet/id=%s&loc=%s&vagon=%s&clasa=%s&pret=%s&utilizator=%s",
                biletId, loc, vagon, clasa, pret, id.toString());

        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();

            headers.set("Authorization", "Bearer " + token);
            headers.set("Content-Type", "application/json");

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> apiResponse = restTemplate
                    .exchange(url, HttpMethod.PUT, entity, String.class);

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

        getDetails(token, biletId, model);
        model.addAttribute("isLogged", isLogged(request));
        return "modificareBilet";
    }

    @PostMapping(path = "modificare/stergere")
    public String handleDelete(@CookieValue("authToken") String token,
                               @RequestParam("id") String id,
                               Model model) {

        Long utilizatorId = getId(token);
        assert utilizatorId != null;
        String url = String.format("http://localhost:8080/api/bilet/id=%s&utilizator=%s",
                id, utilizatorId.toString());

        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();

            headers.set("Authorization", "Bearer " + token);
            headers.set("Content-Type", "application/json");

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> apiResponse = restTemplate
                    .exchange(url, HttpMethod.DELETE, entity, String.class);

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

        getDetails(token, id, model);

        return "redirect:/cont";
    }

    private void getDetails(String token, String biletId, Model model) {

        Long utilizatorId = getId(token);

        assert utilizatorId != null;
        String url = String.format("http://localhost:8080/api/bilet/id=%s&utilizator=%s",
                biletId, utilizatorId.toString());

        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();

            headers.set("Authorization", "Bearer " + token);
            headers.set("Content-Type", "application/json");

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<Bilet> apiResponse = restTemplate
                    .exchange(url, HttpMethod.GET, entity, Bilet.class);

            if (apiResponse.getStatusCode() == HttpStatus.OK) {
                model.addAttribute("bilet", apiResponse.getBody());
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

    private boolean isLogged(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("authToken".equals(cookie.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

}

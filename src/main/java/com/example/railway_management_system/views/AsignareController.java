package com.example.railway_management_system.views;

import com.example.railway_management_system.tren.Tren;
import com.google.gson.Gson;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;

@Controller
public class AsignareController {

    @GetMapping(path = "asignare")
    public String asignare(Model model,
                           HttpServletRequest request) {
        model.addAttribute("isLogged", isLogged(request));
        return "asignare";
    }

    @PostMapping(path = "asignare")
    public String handleAssign(@CookieValue("authToken") String token,
                               @RequestParam("denumire") String denumire,
                               @RequestParam("email") String email,
                               Model model,
                               HttpServletRequest request) {

        String id = "-1";
        String idTren = "-1";

        String url = String.format("http://localhost:8080/api/utilizator/getId/email=%s",
                email);
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();

            headers.set("Authorization", "Bearer " + token);
            headers.set("Content-Type", "application/json");

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> apiResponse = restTemplate
                    .exchange(url, HttpMethod.GET, entity, String.class);
            if (apiResponse.getStatusCode() == HttpStatus.OK) {
                id = apiResponse.getBody();
            }
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                String errorResponse = e.getResponseBodyAsString();
                Gson gson = new Gson();
                Map<String, Object> responseObj = gson.fromJson(errorResponse, Map.class);

                model.addAttribute("error", responseObj.get("message"));
            }
        }

        url = String.format("http://localhost:8080/api/tren/denumire=%s", denumire);

        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();

            headers.set("Authorization", "Bearer " + token);
            headers.set("Content-Type", "application/json");

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<Tren> apiResponse = restTemplate
                    .exchange(url, HttpMethod.GET, entity, Tren.class);
            if (apiResponse.getStatusCode() == HttpStatus.OK) {
                idTren = Objects.requireNonNull(apiResponse.getBody()).getTrenId().toString();
            }
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                String errorResponse = e.getResponseBodyAsString();
                Gson gson = new Gson();
                Map<String, Object> responseObj = gson.fromJson(errorResponse, Map.class);

                model.addAttribute("error", responseObj.get("message"));
            }
        }

        url = String.format("http://localhost:8080/api/utilizator/tren/id=%s&tren=%s",
                id, idTren);

        if(!Objects.equals(id, "-1") && !idTren.equals("-1")) {
            try {
                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();

                headers.set("Authorization", "Bearer " + token);
                headers.set("Content-Type", "application/json");

                HttpEntity<String> entity = new HttpEntity<>(headers);

                ResponseEntity<Tren> apiResponse = restTemplate
                        .exchange(url, HttpMethod.POST, entity, Tren.class);
            } catch (HttpClientErrorException e) {
                if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                    String errorResponse = e.getResponseBodyAsString();
                    Gson gson = new Gson();
                    Map<String, Object> responseObj = gson.fromJson(errorResponse, Map.class);

                    model.addAttribute("error", responseObj.get("message"));
                }
            }
        }

        model.addAttribute("isLogged", isLogged(request));
        return "asignare";
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

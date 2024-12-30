package com.example.railway_management_system.views;

import com.example.railway_management_system.config.JwtService;
import com.example.railway_management_system.utilizator.Utilizator;
import com.google.gson.Gson;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
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

@Controller
public class ContController {

    private final JwtService jwtService;
    private Long id;

    @Autowired
    public ContController(JwtService jwtService) {
        this.jwtService = jwtService;
        this.id = 0L;
    }

    @GetMapping(path = "cont")
    public String cont(@CookieValue("authToken") String token,
                       Model model) {

        getDetails(token, model);

        return "cont";
    }

    @PostMapping(path = "cont")
    public String handleUpdateAccount(@CookieValue("authToken") String token,
                                      @RequestParam("nume") String nume,
                                      @RequestParam("prenume") String prenume,
                                      @RequestParam("email") String email,
                                      @RequestParam("telefon") String telefon,
                                      Model model,
                                      HttpServletResponse response) {

        String base = "http://localhost:8080/api/utilizator/";
        String url = String.format(base + "id=%s&nume=%s" +
                "&prenume=%s&email=%s&telefon=%s", id.toString(), nume, prenume, email, telefon);

        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();

            headers.set("Authorization", "Bearer " + token);
            headers.set("Content-Type", "application/json");

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> apiResponse = restTemplate
                    .exchange(url, HttpMethod.PUT, entity, String.class);

            if (apiResponse.getStatusCode() == HttpStatus.OK) {
                Cookie cookie = new Cookie("authToken", null);
                cookie.setMaxAge(0);
                cookie.setPath("/");
                response.addCookie(cookie);
                return "redirect:login";
            }
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                String errorResponse = e.getResponseBodyAsString();
                System.out.println(errorResponse);
                Gson gson = new Gson();
                Map<String, Object> responseObj = gson.fromJson(errorResponse, Map.class);

                model.addAttribute("error", responseObj.get("message"));
            }
        }

        getDetails(token, model);

        return "cont";
    }

    private void getDetails(String token, Model model) {
        String email = jwtService.extractEmail(token);
        String url = String.format("http://localhost:8080/api/utilizator/email=%s", email);
        System.out.println(url);

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
                model.addAttribute("cont", cont);
                assert cont != null;
                this.id = cont.getUtilizatorId();
            }
        } catch (HttpClientErrorException _) {

        }
    }

}

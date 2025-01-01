package com.example.railway_management_system.views;

import com.example.railway_management_system.auth.AuthRequest;
import com.example.railway_management_system.auth.AuthResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Controller
public class LoginController {

    @GetMapping(path = "login")
    public String login(Model model) {
        return "login";
    }

    @PostMapping(path = "login")
    public String handleLogin(@ModelAttribute AuthRequest authRequest,
                              Model model,
                              HttpServletResponse response) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            ResponseEntity<AuthResponse> apiResponse = restTemplate
                    .postForEntity("http://localhost:8080/api/auth/authenticate",
                            authRequest, AuthResponse.class);
            if (apiResponse.getStatusCode() == HttpStatus.OK) {
                String token = Objects.requireNonNull(apiResponse.getBody()).getToken();

                Cookie tokenCookie = new Cookie("authToken", token);
                tokenCookie.setHttpOnly(true);
                tokenCookie.setSecure(true);
                tokenCookie.setPath("/");
                response.addCookie(tokenCookie);
                return "redirect:/";
            }

        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.FORBIDDEN) {
                model.addAttribute("error", "email sau parola incorecte");
            }
        }
        return "login";
    }
}

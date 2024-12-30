package com.example.railway_management_system.views;

import com.example.railway_management_system.auth.AuthResponse;
import com.example.railway_management_system.auth.RegisterRequest;
import com.google.gson.Gson;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;

@Controller
public class SignupController {

    @GetMapping(path = "signup")
    public String login(Model model) {
        return "signup";
    }

    @PostMapping(path = "signup")
    public String handleSignup(@ModelAttribute RegisterRequest registerRequest,
                               Model model,
                               HttpServletResponse response) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            ResponseEntity<AuthResponse> apiResponse = restTemplate
                    .postForEntity("http://localhost:8080/api/auth/register",
                            registerRequest, AuthResponse.class);
            if (apiResponse.getStatusCode() == HttpStatus.OK) {
                String token = Objects.requireNonNull(apiResponse.getBody()).getToken();

                Cookie tokenCookie = new Cookie("authToken", token);
                tokenCookie.setHttpOnly(true);
                tokenCookie.setSecure(true);
                tokenCookie.setPath("/");
                response.addCookie(tokenCookie);
            }
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                String errorResponse = e.getResponseBodyAsString();
                Gson gson = new Gson();
                Map<String, Object> responseObj = gson.fromJson(errorResponse, Map.class);

                model.addAttribute("error", responseObj.get("message"));
            }
        }

        return "signup";
    }
}

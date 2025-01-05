/** Clasa pentru HomeController
 * @author Stanescu Stefan
 * @version 10 Decembrie 2024
 */

package com.example.railway_management_system.views;

import com.example.railway_management_system.program.Program;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Controller
public class HomeController {

    @GetMapping(path = "/")
    public String home(Model model,
                       HttpServletRequest request) {
        model.addAttribute("isLogged", isLogged(request));
        return "home";
    }

    @PostMapping(path = "/")
    public String handleCautare(@RequestParam("statiePlecare") String statiePlecare,
                                @RequestParam("statieDestinatie") String statieDestinatie,
                                HttpServletRequest request,
                                Model model) {

        try {

            String base = "http://localhost:8080/api/ruta/cautare";
            String url = String.format(base + "/plecare=%s&destinatie=%s",
                    statiePlecare, statieDestinatie);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<List<Program>> apiResponse = restTemplate.exchange(
                    url,
                    org.springframework.http.HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Program>>() {}
            );
            if (apiResponse.getStatusCode() == HttpStatus.OK) {
                List<Program> programe = apiResponse.getBody();
                model.addAttribute("programe", programe);
            }

        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                model.addAttribute("error", "Ruta inexistenta");
                try {
                    String base = "http://localhost:8080/api/ruta/cautare/statii";
                    String url = String.format(base + "/plecare=%s&destinatie=%s",
                            statiePlecare, statieDestinatie);

                    RestTemplate restTemplate = new RestTemplate();
                    ResponseEntity<List<String>> apiResponse = restTemplate.exchange(
                            url,
                            org.springframework.http.HttpMethod.GET,
                            null,
                            new ParameterizedTypeReference<List<String>>() {
                            }
                    );
                    if (apiResponse.getStatusCode() == HttpStatus.OK) {
                        List<String> statii = apiResponse.getBody();
                        assert statii != null;
                        if (!statii.isEmpty()) {
                            model.addAttribute("statii", statii);
                        }
                    }
                } catch (HttpClientErrorException _) {

                }

            }
        }

        model.addAttribute("isLogged", isLogged(request));
        return "home";
    }

    @GetMapping(path = "/cont/logout")
    public String handleLogout(HttpServletResponse response) {
        Cookie cookie = new Cookie("authToken", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return "redirect:/login";
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

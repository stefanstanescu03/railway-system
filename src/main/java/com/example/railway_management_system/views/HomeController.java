package com.example.railway_management_system.views;

import com.example.railway_management_system.program.Program;
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
    public String home() {
        return "home";
    }

    @PostMapping(path = "/")
    public String handleCautare(@RequestParam("statiePlecare") String statiePlecare,
                                @RequestParam("statieDestinatie") String statieDestinatie,
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

        return "home";
    }
}

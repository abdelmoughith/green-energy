package pack.greenenergy.controllers.users;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import pack.greenenergy.dtos.users.LoginRequest;
import pack.greenenergy.dtos.users.RegisterRequest;
import pack.greenenergy.dtos.users.AuthResponse;

@Controller
@RequiredArgsConstructor
@RequestMapping("/web")
public class WebHtmlAuthController {

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/register")
    public String showRegister() {
        return "register";
    }

    @PostMapping("/register")
    public String doRegister(@RequestParam String email,
                             @RequestParam String password,
                             Model model) {
        try {
            RegisterRequest request = new RegisterRequest(email, password);
            ResponseEntity<String> response = restTemplate.postForEntity(
                    "http://localhost:8080/auth/register", request, String.class
            );
            model.addAttribute("message", response.getBody());
        } catch (Exception e) {
            model.addAttribute("message", "Error: " + e.getMessage());
        }
        return "register";
    }

    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String email,
                          @RequestParam String password,
                          Model model) {
        try {
            LoginRequest request = new LoginRequest(email, password);
            ResponseEntity<AuthResponse> response = restTemplate.postForEntity(
                    "http://localhost:8080/auth/login", request, AuthResponse.class
            );
            model.addAttribute("token", response.getBody().token());
        } catch (Exception e) {
            model.addAttribute("message", "Login failed: " + e.getMessage());
        }
        return "login";
    }
}


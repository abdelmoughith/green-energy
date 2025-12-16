package pack.greenenergy.controllers.users;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pack.greenenergy.dtos.users.LoginRequest;
import pack.greenenergy.dtos.users.RegisterRequest;
import pack.greenenergy.dtos.users.AuthResponse;
import pack.greenenergy.entities.users.Role;
import pack.greenenergy.entities.users.User;
import pack.greenenergy.exception.ValidationException;
import pack.greenenergy.security.JwtUtils;
import pack.greenenergy.services.users.CustomUserService;
import pack.greenenergy.services.users.RoleService;

import java.util.Set;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserService customUserService;
    private final RoleService roleService;


    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
        if (customUserService.existsByEmail(request.username())) {
            throw new ValidationException("Email already exists");
        }

        User user = new User();
        user.setEmail(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        Role role = roleService.findByName("USER");
        user.setRoles(Set.of(role));

        customUserService.save(user);

        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {

        User user = customUserService.findByEmail(request.username());
        // Authentication authentication =
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        String jwt = jwtUtils.generateToken(user);
        return ResponseEntity.ok(new AuthResponse(jwt));
    }
}

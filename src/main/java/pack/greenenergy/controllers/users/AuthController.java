package pack.greenenergy.controllers.users;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pack.greenenergy.dtos.users.LoginRequest;
import pack.greenenergy.dtos.users.RegisterRequest;
import pack.greenenergy.dtos.users.AuthResponse;
import pack.greenenergy.entities.users.Role;
import pack.greenenergy.entities.users.User;
import pack.greenenergy.security.JwtUtils;
import pack.greenenergy.services.users.CustomUserService;
import pack.greenenergy.services.users.RoleService;
import pack.greenenergy.services.users.UserMapper;

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
    private final UserMapper userMapper;


    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        if (customUserService.existsByEmail(request.email())) {
            return ResponseEntity.badRequest().body("email already exists");
        }

        User user = new User();
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setEmail(request.email());
        Role role = roleService.findByName("USER");
        user.setRoles(Set.of(role));

        customUserService.save(user);

        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        UserDetails userDetails = customUserService.loadUserByUsername(request.email());
        User user = userMapper.toUser(userDetails);
        String jwt = jwtUtils.generateToken(user);
        return ResponseEntity.ok(new AuthResponse(jwt));
    }
}

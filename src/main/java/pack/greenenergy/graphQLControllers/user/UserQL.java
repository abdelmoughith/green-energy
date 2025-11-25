package pack.greenenergy.graphQLControllers.user;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pack.greenenergy.dtos.users.AuthResponse;
import pack.greenenergy.dtos.users.LoginRequest;
import pack.greenenergy.entities.users.User;
import pack.greenenergy.security.JwtUtils;
import pack.greenenergy.services.users.CustomUserService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserQL {

    private final CustomUserService customUserService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    // TODO QUERY MAPPING

    @QueryMapping
    @PreAuthorize("permitAll()")
    public List<User> getAllUsers() {
        return customUserService.getAllUsers();
    }


    // TODO MUTATION MAPPING
    @MutationMapping
    @PreAuthorize("permitAll()")
    public AuthResponse login(@Argument String email, @Argument String password) {

        User user = customUserService.findByEmail(email);
        // Authentication authentication =
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        String jwt = jwtUtils.generateToken(user);
        return new AuthResponse(jwt);
    }
    /**
    @MutationMapping
    @PreAuthorize("permitAll()")
    public AuthResponse login(@Argument("input") LoginRequest request) {
        String email = request.email();
        String password = request.password();
        // ...
    }
    ***/


}


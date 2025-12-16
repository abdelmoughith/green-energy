package pack.greenenergy.controllers.users;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pack.greenenergy.entities.users.User;
import pack.greenenergy.security.JwtUtils;
import pack.greenenergy.services.users.CustomUserService;

import java.security.Principal;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final CustomUserService userService;
    private final JwtUtils jwtUtils;
    private final CustomUserService customUserService;

    private Long extractUserIdFromRequest(HttpServletRequest req) {
        String auth = req.getHeader("Authorization");
        if (auth == null || !auth.startsWith("Bearer ")) return null;
        return jwtUtils.extractUserId(auth.substring(7));
    }

    /**
     * Get my profile
     */
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public User getCurrentUser(@AuthenticationPrincipal User currentUser) {
        return currentUser;
    }

    /**
     * Get any user (ADMIN only)
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public User getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping
    public String test(Principal principal) {
        return principal.getName();
    }

    @GetMapping("/user")
    public ResponseEntity<User> test(HttpServletRequest request) {
        Long userId = extractUserIdFromRequest(request);
        if (userId == null) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }
        return ResponseEntity.ok(customUserService.getUserById(userId));
    }


}


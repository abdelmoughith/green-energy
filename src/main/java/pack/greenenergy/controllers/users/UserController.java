package pack.greenenergy.controllers.users;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pack.greenenergy.entities.users.User;
import pack.greenenergy.services.users.CustomUserService;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final CustomUserService userService;

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

}


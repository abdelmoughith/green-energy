package pack.greenenergy.controllers.projects;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pack.greenenergy.entities.projects.Project;
import pack.greenenergy.security.JwtUtils;
import pack.greenenergy.services.projects.ProjectService;
import pack.greenenergy.services.users.CustomUserService;

import java.util.Set;

@RestController
@RequestMapping("/save")
@RequiredArgsConstructor
public class PostSaverController {

    private final ProjectService projectService;
    private final CustomUserService userService;
    private final JwtUtils jwtUtil;

    @PostMapping("/save/{projectId}")
    public ResponseEntity<String> saveAnnonce(@RequestHeader("Authorization") String token,
                                              @PathVariable Long projectId) {
        String username = jwtUtil.extractUsername(token.replace("Bearer ", ""));
        Long userId = userService.loadUserByUsername(username).getId();

        boolean success = projectService.saveAnnonce(userId, projectId);
        return success ?
                ResponseEntity.ok("Project saved!") :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("User or project not found.");
    }

    @DeleteMapping("/unsave/{projectId}")
    public ResponseEntity<String> unsaveAnnonce(@RequestHeader("Authorization") String token,
                                                @PathVariable Long projectId) {
        String username = jwtUtil.extractUsername(token.replace("Bearer ", ""));
        Long userId = userService.loadUserByUsername(username).getId();

        boolean success = projectService.unsaveAnnonce(userId, projectId);
        return success ?
                ResponseEntity.ok("Project unsaved.") :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("User or project not found.");
    }

    @GetMapping("/saved")
    public ResponseEntity<?> getSavedAnnonces(@RequestHeader("Authorization") String token) {
        String username = jwtUtil.extractUsername(token.replace("Bearer ", ""));
        Long userId = userService.loadUserByUsername(username).getId();

        Set<Project> annonces = projectService.getSavedAnnonces(userId);
        return ResponseEntity.ok(annonces);
    }
}

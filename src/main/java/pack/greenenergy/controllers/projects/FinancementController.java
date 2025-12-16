package pack.greenenergy.controllers.projects;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pack.greenenergy.dtos.projects.FinancementRequest;
import pack.greenenergy.dtos.projects.FinancementResponse;
import pack.greenenergy.dtos.projects.FinancementResponseFormated;
import pack.greenenergy.entities.projects.Financement;
import pack.greenenergy.security.JwtUtils;
import pack.greenenergy.services.projects.FinancementService;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

@RestController
@RequestMapping("/financement")
@RequiredArgsConstructor
public class FinancementController {

    private final FinancementService financementService;
    private final JwtUtils jwtUtils;

    private Long extractUserIdFromRequest(HttpServletRequest req) {
        String auth = req.getHeader("Authorization");
        if (auth == null || !auth.startsWith("Bearer ")) return null;
        return jwtUtils.extractUserId(auth.substring(7));
    }

    // ---------------- CREATE ----------------
    @PostMapping("/project/{projectId}")
    public ResponseEntity<FinancementResponse> financer(
            @PathVariable Long projectId,
            @RequestBody FinancementRequest req,
            HttpServletRequest request
    ) {
        Long uid = extractUserIdFromRequest(request);
        if (uid == null) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }

        Financement f = financementService.financerProjet(projectId, uid, req.getMontant());

        return ResponseEntity.ok(toResponse(f));
    }


    // ---------------- GET ALL ----------------
    @GetMapping
    public ResponseEntity<List<FinancementResponse>> getAll() {
        return ResponseEntity.ok(
                financementService.getAllFinancements().stream()
                        .map(this::toResponse)
                        .toList()
        );
    }
    @GetMapping("/formated")
    public ResponseEntity<List<FinancementResponseFormated>> getAllFormated() {
        return ResponseEntity.ok(
                financementService.getAllFinancements().stream()
                        .map(this::toResponseFormated)
                        .toList()
        );
    }

    @GetMapping("/formated/mine")
    public ResponseEntity<List<FinancementResponseFormated>> getAllFormated(
            HttpServletRequest request
    ) {
        Long ownerId = extractUserIdFromRequest(request);
        return ResponseEntity.ok(
                financementService.getAllFinancements().stream()
                        .map(this::toResponseFormated)
                        .filter(f -> f.getUserId().equals(ownerId))
                        .toList()
        );
    }
    // ---------------- GET BY PROJECT ----------------
    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<FinancementResponseFormated>> getByProject(@PathVariable Long projectId) {
        return ResponseEntity.ok(
                financementService.getFinancementsByProject(projectId).stream()
                        .map(this::toResponseFormated)
                        .toList()
        );
    }

    // ---------------- GET BY USER ----------------
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FinancementResponse>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(
                financementService.getFinancementsByUser(userId).stream()
                        .map(this::toResponse)
                        .toList()
        );
    }

    // ---------------- MAPPER ----------------
    private FinancementResponse toResponse(Financement f) {
        FinancementResponse r = new FinancementResponse();
        r.setId(f.getId());
        r.setProjectId(f.getProject().getId());
        r.setUserId(f.getUser().getId());
        r.setMontant(f.getMontant());
        r.setDateFinancement(f.getDateFinancement());
        return r;
    }

    private FinancementResponseFormated toResponseFormated(Financement financement) {
        FinancementResponseFormated response = new FinancementResponseFormated();
        response.setId(financement.getId());
        response.setProjectId(financement.getProject().getId());
        response.setUserId(financement.getUser().getId());
        response.setMontant(financement.getMontant());
        response.setDateFinancement(financement.getDateFinancement());

        // Fetch user and project from repositories
        String username = financement.getUser().getUsername();

        String projectTitle = financement.getProject().getTitre();

        response.setMessage(String.format("%s has financed project: %s with %.2f MAD",
                username, projectTitle, financement.getMontant()));

        return response;
    }

}

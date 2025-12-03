package pack.greenenergy.controllers.projects;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pack.greenenergy.dtos.projects.FinancementRequest;
import pack.greenenergy.dtos.projects.FinancementResponse;
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

    private Long userId(HttpServletRequest req) {
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
        Long uid = userId(request);

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

    // ---------------- GET BY PROJECT ----------------
    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<FinancementResponse>> getByProject(@PathVariable Long projectId) {
        return ResponseEntity.ok(
                financementService.getFinancementsByProject(projectId).stream()
                        .map(this::toResponse)
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
}

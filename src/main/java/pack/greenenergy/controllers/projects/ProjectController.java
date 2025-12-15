package pack.greenenergy.controllers.projects;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pack.greenenergy.dtos.projects.ProjectRequest;
import pack.greenenergy.dtos.projects.ProjectResponse;
import pack.greenenergy.dtos.projects.ProjectWithDistance;
import pack.greenenergy.entities.projects.Project;
import pack.greenenergy.exception.ForbiddenException;
import pack.greenenergy.security.JwtUtils;
import pack.greenenergy.services.projects.ProjectService;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final JwtUtils jwtUtils;

    private Long getUserId(HttpServletRequest req) {
        String authHeader = req.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ForbiddenException("Authorization token missing or invalid");
        }
        String token = authHeader.substring(7);
        return jwtUtils.extractUserId(token);
    }

    // CREATE
    /*
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<ProjectResponse> createProject(
            @RequestParam("project") String dtoJson,
            @RequestParam(value = "image", required = true) MultipartFile image,
            HttpServletRequest request
    ) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ProjectRequest dto = objectMapper.readValue(dtoJson, ProjectRequest.class);

        Long ownerId = getUserId(request);
        Project p = projectService.createProject(dto, ownerId, image);
        return ResponseEntity.ok(toResponse(p));
    }

     */
    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(
            @RequestBody ProjectRequest dto,
            HttpServletRequest request
    ) throws IOException {
        Long ownerId = getUserId(request);
        Project p = projectService.createProject(dto, ownerId, null);
        return ResponseEntity.ok(toResponse(p));
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<ProjectResponse>> getAllProjects() {
        return ResponseEntity.ok(
                projectService.getAllProjects().stream()
                        .map(this::toResponse)
                        .toList()
        );
    }

    // READ ONE
    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProject(@PathVariable Long id) {
        return ResponseEntity.ok(toResponse(projectService.getProjectById(id)));
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponse> updateProject(
            @PathVariable Long id,
            @RequestBody ProjectRequest dto,
            HttpServletRequest request
    ) {
        Long userId = getUserId(request);
        Project updated = projectService.updateProject(id, dto, userId);
        return ResponseEntity.ok(toResponse(updated));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProject(
            @PathVariable Long id,
            HttpServletRequest request
    ) {
        Long userId = getUserId(request);
        projectService.deleteProject(id, userId);
        return ResponseEntity.noContent().build();
    }

    // ---------------- Mapping ----------------
    private ProjectResponse toResponse(Project p) {
        ProjectResponse r = new ProjectResponse();
        r.setId(p.getId());
        r.setTitre(p.getTitre());
        r.setDescription(p.getDescription());
        r.setRegion(p.getRegion());
        r.setMontantRequis(p.getMontantRequis());
        r.setMontantCollecte(p.getMontantCollecte());
        r.setStatusProjet(p.getStatut().name());
        r.setTypeEnergie(p.getTypeEnergie().name());
        r.setProprietaireId(p.getProprietaire().getId());
        r.setProprietaireUsername(p.getProprietaire().getUsername());
        r.setDateCreation(p.getDateCreation());
        r.setDateValidation(p.getDateValidation());
        r.setLatitude(p.getLatitude());
        r.setLongitude(p.getLongitude());
        return r;
    }
    @GetMapping("/nearby")
    public List<ProjectWithDistance> getNearbyProjects(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam int distanceKm
    ) {
        return projectService.getNearbyProjectsWithDistance(latitude, longitude, distanceKm);
    }
}

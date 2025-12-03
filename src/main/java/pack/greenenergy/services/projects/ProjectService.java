package pack.greenenergy.services.projects;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pack.greenenergy.dtos.projects.ProjectRequest;
import pack.greenenergy.entities.projects.Project;
import pack.greenenergy.entities.projects.StatutProjet;
import pack.greenenergy.entities.projects.TypeEnergie;
import pack.greenenergy.entities.users.User;
import pack.greenenergy.exception.ForbiddenException;
import pack.greenenergy.exception.ResourceNotFoundException;
import pack.greenenergy.repositories.projects.ProjectRepository;
import pack.greenenergy.repositories.users.UserRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    // ---------------- CREATE ----------------
    public Project createProject(ProjectRequest dto, Long ownerId) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found"));

        Project p = new Project();
        p.setTitre(dto.getTitre());
        p.setDescription(dto.getDescription());
        p.setRegion(dto.getRegion());
        p.setMontantRequis(dto.getMontantRequis());

        // convert string -> enum
        p.setStatut(StatutProjet.valueOf(dto.getStatusProjet().toUpperCase()));
        p.setTypeEnergie(TypeEnergie.valueOf(dto.getTypeEnergie().toUpperCase()));

        p.setDateValidation(dto.getDateValidation());
        p.setProprietaire(owner);
        p.setDateCreation(java.time.LocalDateTime.now());
        p.setMontantCollecte(0.0);

        return projectRepository.save(p);
    }

    // ---------------- READ ----------------
    public Project getProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    // ---------------- UPDATE ----------------
    public Project updateProject(Long projectId, ProjectRequest dto, Long userId) {
        Project p = getProjectById(projectId);

        if (!p.getProprietaire().getId().equals(userId)) {
            throw new ForbiddenException("You are not the owner of this project");
        }

        p.setTitre(dto.getTitre());
        p.setDescription(dto.getDescription());
        p.setRegion(dto.getRegion());
        p.setMontantRequis(dto.getMontantRequis());

        // convert string -> enum
        p.setStatut(StatutProjet.valueOf(dto.getStatusProjet().toUpperCase()));
        p.setTypeEnergie(TypeEnergie.valueOf(dto.getTypeEnergie().toUpperCase()));
        p.setDateValidation(dto.getDateValidation());

        return projectRepository.save(p);
    }

    // ---------------- DELETE ----------------
    public void deleteProject(Long id, Long userId) {
        Project p = getProjectById(id);
        if (!p.getProprietaire().getId().equals(userId)) {
            throw new ForbiddenException("You are not the owner of this project");
        }
        projectRepository.delete(p);
    }

    // ---------------- OWNERSHIP ----------------
    public boolean isProjectOwner(Long projectId, Long userId) {
        return projectRepository.findById(projectId)
                .map(p -> p.getProprietaire().getId().equals(userId))
                .orElse(false);
    }
}

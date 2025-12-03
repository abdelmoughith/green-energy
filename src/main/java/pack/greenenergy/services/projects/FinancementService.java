package pack.greenenergy.services.projects;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pack.greenenergy.entities.projects.Financement;
import pack.greenenergy.entities.projects.Project;
import pack.greenenergy.entities.users.User;
import pack.greenenergy.exception.ResourceNotFoundException;
import pack.greenenergy.exception.DuplicateResourceException;
import pack.greenenergy.repositories.projects.FinancementRepository;
import pack.greenenergy.repositories.projects.ProjectRepository;
import pack.greenenergy.repositories.users.UserRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class FinancementService {

    private final FinancementRepository financementRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public Financement financerProjet(Long projectId, Long userId, Double montant) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Financement f = new Financement();
        f.setUser(user);
        f.setProject(project);
        f.setMontant(montant);
        f.setDateFinancement(java.time.LocalDateTime.now());

        project.setMontantCollecte(
                (project.getMontantCollecte() == null ? 0 : project.getMontantCollecte()) + montant
        );

        return financementRepository.save(f);
    }

    // ---------------- GET ALL ----------------
    public List<Financement> getAllFinancements() {
        return financementRepository.findAll();
    }

    // ---------------- GET BY PROJECT ----------------
    public List<Financement> getFinancementsByProject(Long projectId) {
        return financementRepository.findAll().stream()
                .filter(f -> f.getProject().getId().equals(projectId))
                .toList();
    }

    // ---------------- GET BY USER ----------------
    public List<Financement> getFinancementsByUser(Long userId) {
        return financementRepository.findAll().stream()
                .filter(f -> f.getUser().getId().equals(userId))
                .toList();
    }
}

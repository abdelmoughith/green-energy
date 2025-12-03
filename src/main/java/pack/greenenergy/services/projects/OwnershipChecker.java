package pack.greenenergy.services.projects;

import org.springframework.stereotype.Component;
import pack.greenenergy.repositories.projects.ProjectRepository;

@Component
public class OwnershipChecker {

    private final ProjectRepository projectRepository;

    public OwnershipChecker(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public boolean isProjectOwner(Long projectId, Long userId) {
        return projectRepository
                .findById(projectId)
                .map(p -> p.getProprietaire().getId().equals(userId))
                .orElse(false);
    }
}


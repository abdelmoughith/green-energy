package pack.greenenergy.services.projects;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pack.greenenergy.dtos.projects.ProjectRequest;
import pack.greenenergy.dtos.projects.ProjectWithDistance;
import pack.greenenergy.entities.projects.Project;
import pack.greenenergy.entities.projects.StatutProjet;
import pack.greenenergy.entities.projects.TypeEnergie;
import pack.greenenergy.entities.users.User;
import pack.greenenergy.exception.ForbiddenException;
import pack.greenenergy.exception.ResourceNotFoundException;
import pack.greenenergy.repositories.projects.ProjectRepository;
import pack.greenenergy.repositories.users.UserRepository;
import pack.greenenergy.services.firebase.ImageStorageServiceCloud;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ImageStorageServiceCloud imageStorageServiceCloud;

    // ---------------- CREATE ----------------
    @Transactional
    public Project createProject(
            ProjectRequest dto,
            Long ownerId,
            MultipartFile image
    ) throws IOException {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found"));

        Project p = new Project();
        p.setUser(owner);
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
        p.setLatitude(dto.getLatitude());
        p.setLongitude(dto.getLongitude());


        if (image != null) {
            String imageUrl = imageStorageServiceCloud.saveImage(image);

            p.setImageUrl(
                    imageUrl
            );
        }


        return projectRepository.save(p);
    }

    // ---------------- READ ----------------
    public Project getProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAllByOrderByDateCreationDesc();
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

    // SAVING
    public boolean saveAnnonce(Long userId, Long annonceId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Project> annonceOpt = projectRepository.findById(annonceId);

        if (userOpt.isPresent() && annonceOpt.isPresent()) {
            User user = userOpt.get();
            Project annonce = annonceOpt.get();
            user.getSavedProjects().add(annonce);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    // unsave
    public boolean unsaveAnnonce(Long userId, Long annonceId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Project> annonceOpt = projectRepository.findById(annonceId);

        if (userOpt.isPresent() && annonceOpt.isPresent()) {
            User user = userOpt.get();
            Project annonce = annonceOpt.get();

            user.getSavedProjects().remove(annonce);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    // get saved post
    public Set<Project> getSavedAnnonces(Long userId) {
        return userRepository.findById(userId)
                .map(User::getSavedProjects)
                .orElse(Collections.emptySet());
    }
    // get by distance
    public List<ProjectWithDistance> getNearbyProjectsWithDistance(Double latitude, Double longitude, int distanceKm) {
        return projectRepository.findNearbyProjectsWithDistance(latitude, longitude, (double) distanceKm);
    }


}

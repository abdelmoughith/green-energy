package pack.greenenergy.dtos.projects;

import lombok.Data;
import pack.greenenergy.entities.projects.StatutProjet;
import pack.greenenergy.entities.projects.TypeEnergie;

import java.time.LocalDateTime;

@Data
public class ProjectRequest {

    private String titre;
    private String description;
    private String region;
    private Double montantRequis;

    // Strings instead of enums
    private String statusProjet;
    private String typeEnergie;

    private LocalDateTime dateValidation;
}

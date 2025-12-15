package pack.greenenergy.dtos.projects;

import lombok.Data;
import java.time.LocalDateTime;
@Data
public class ProjectResponse {
    private Long id;
    private String titre;
    private String description;
    private String region;
    private Double montantRequis;
    private Double montantCollecte;
    private String statusProjet;
    private String typeEnergie;
    private Long proprietaireId;
    private String proprietaireUsername;
    private LocalDateTime dateCreation;
    private LocalDateTime dateValidation;
    private Double latitude;
    private Double longitude;
}



package pack.greenenergy.entities.projects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import pack.greenenergy.entities.users.User;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;
    private String description;
    private String region;
    private Double montantRequis;
    private Double montantCollecte;
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private StatutProjet statut;

    @Enumerated(EnumType.STRING)
    private TypeEnergie typeEnergie;

    private LocalDateTime dateCreation;
    private LocalDateTime dateValidation;

    // for location
    private Double latitude;
    private Double longitude;

    // OWNER OF PROJECT
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proprietaire_id", nullable = false)
    @JsonIgnore
    @JsonIgnoreProperties({"savedProjects"})
    private User proprietaire;

    // INVESTMENTS
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Financement> financements;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"savedProjects"})
    private User user;

}

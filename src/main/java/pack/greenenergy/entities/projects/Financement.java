package pack.greenenergy.entities.projects;

import jakarta.persistence.*;
import lombok.*;
import pack.greenenergy.entities.users.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "project_investors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Financement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Double montant; // investment amount
    private LocalDateTime dateFinancement; // investment date
}

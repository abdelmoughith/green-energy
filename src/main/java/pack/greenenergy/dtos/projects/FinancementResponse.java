package pack.greenenergy.dtos.projects;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FinancementResponse {

    private Long id;
    private Long userId;
    private Long projectId;

    private Double montant;
    private LocalDateTime dateFinancement;
}


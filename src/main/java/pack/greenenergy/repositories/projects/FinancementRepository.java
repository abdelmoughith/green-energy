package pack.greenenergy.repositories.projects;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pack.greenenergy.entities.projects.Financement;

import java.util.List;


@Repository
public interface FinancementRepository extends JpaRepository<Financement, Long> {
    List<Financement> findByProjectId(Long projectId);

    List<Financement> findByUserId(Long userId);

}

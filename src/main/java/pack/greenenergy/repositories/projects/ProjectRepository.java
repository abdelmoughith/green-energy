package pack.greenenergy.repositories.projects;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pack.greenenergy.dtos.projects.ProjectWithDistance;
import pack.greenenergy.entities.projects.Project;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByProprietaireId(Long userId);

    @Query("""
    SELECT p AS project,
           (6371 * acos(
               cos(radians(:lat)) * cos(radians(p.latitude)) *
               cos(radians(p.longitude) - radians(:lng)) +
               sin(radians(:lat)) * sin(radians(p.latitude))
           )) AS distanceKm
    FROM Project p
    WHERE (6371 * acos(
               cos(radians(:lat)) * cos(radians(p.latitude)) *
               cos(radians(p.longitude) - radians(:lng)) +
               sin(radians(:lat)) * sin(radians(p.latitude))
           )) <= :distance
    ORDER BY distanceKm ASC
    """)
    List<ProjectWithDistance> findNearbyProjectsWithDistance(
            @Param("lat") Double latitude,
            @Param("lng") Double longitude,
            @Param("distance") Double distanceKm
    );



}

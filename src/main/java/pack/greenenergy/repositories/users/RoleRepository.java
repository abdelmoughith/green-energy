package pack.greenenergy.repositories.users;

import org.springframework.data.jpa.repository.JpaRepository;
import pack.greenenergy.entities.users.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
    boolean existsByName(String name);
}

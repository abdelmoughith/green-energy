package pack.greenenergy.repositories.users;

import org.springframework.data.jpa.repository.JpaRepository;
import pack.greenenergy.entities.users.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}

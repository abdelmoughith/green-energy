package pack.greenenergy.services.users;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pack.greenenergy.entities.users.User;
import pack.greenenergy.exception.ResourceNotFoundException;
import pack.greenenergy.repositories.users.UserRepository;

@Service
@RequiredArgsConstructor
public class CustomUserService implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(
                        () -> new ResourceNotFoundException("User not found with email: " + username)
                );
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new ResourceNotFoundException("User not found with email: " + email)
                );
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

}

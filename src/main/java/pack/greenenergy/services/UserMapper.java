package pack.greenenergy.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import pack.greenenergy.entities.User;

@Service
public class UserMapper {

    public User toUser(UserDetails userDetails) {
        if (userDetails instanceof User user) {
            return user;
        }
        throw new IllegalArgumentException("Cannot convert UserDetails to User");
    }
}


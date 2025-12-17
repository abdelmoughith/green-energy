package pack.greenenergy.dtos.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import pack.greenenergy.dtos.validage.Adult;

import java.time.LocalDate;

public record RegisterRequest(

        @NotBlank(message = "Username is required")
        String email,
        @NotBlank(message = "Password is required")
        @Size(min = 6, message = "Password must be at least 6 characters")
        String password,

        String firstName,
        String lastName,
        String phoneNumber,
        String address,

        @Adult
        LocalDate birthday

) {}

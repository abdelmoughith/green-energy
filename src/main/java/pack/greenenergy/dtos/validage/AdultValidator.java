package pack.greenenergy.dtos.validage;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;

public class AdultValidator implements ConstraintValidator<Adult, LocalDate> {

    @Override
    public boolean isValid(LocalDate birthday, ConstraintValidatorContext context) {
        if (birthday == null) {
            return true; // optional field
        }
        return Period.between(birthday, LocalDate.now()).getYears() >= 18;
    }
}


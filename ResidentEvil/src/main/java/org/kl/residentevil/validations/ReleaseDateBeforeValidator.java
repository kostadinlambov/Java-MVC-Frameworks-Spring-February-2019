package org.kl.residentevil.validations;

import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

@Component
public class ReleaseDateBeforeValidator implements ConstraintValidator<ReleaseDateBefore, LocalDate> {

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        try {
            if(localDate != null){
                LocalDate today = LocalDate.now();
                return localDate.isBefore(today);
            }

            return false;
        }catch(Exception e){
            throw new IllegalArgumentException("Invalid date");
        }
    }
}


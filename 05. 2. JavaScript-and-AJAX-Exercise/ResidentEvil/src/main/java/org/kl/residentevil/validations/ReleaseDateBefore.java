package org.kl.residentevil.validations;

import org.springframework.stereotype.Component;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Component
@Constraint(validatedBy = ReleaseDateBeforeValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ReleaseDateBefore {
    String message() default "Invalid date.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload()default {};
}

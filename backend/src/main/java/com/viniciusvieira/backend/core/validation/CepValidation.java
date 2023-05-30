package com.viniciusvieira.backend.core.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { CepValidator.class })
public @interface CepValidation {
    String message() default "CEP inválido...";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

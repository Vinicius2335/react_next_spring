package com.viniciusvieira.backend.core.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE}) // TODO - pode dá erro, está diferente do que eu testei { FIELD, PARAMETER }
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {CpfValidator.class})
public @interface CpfValidation {
    String message() default "CPF inválido...";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

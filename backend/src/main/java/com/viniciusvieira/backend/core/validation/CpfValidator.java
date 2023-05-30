package com.viniciusvieira.backend.core.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CpfValidator implements ConstraintValidator<CpfValidation, String> {

    @Override
    public boolean isValid(String cpf, ConstraintValidatorContext context) {
        String regex = "[0-9]{3}\\.?[0-9]{3}\\.?[0-9]{3}\\-?[0-9]{2}";
        return cpf.matches(regex);
    }
}

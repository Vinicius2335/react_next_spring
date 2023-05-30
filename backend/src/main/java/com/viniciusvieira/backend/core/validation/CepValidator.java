package com.viniciusvieira.backend.core.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CepValidator implements ConstraintValidator<CepValidation, String> {

    @Override
    public boolean isValid(String cep, ConstraintValidatorContext context) {
        if (cep == null){
            return false;
        } else {
            String regex = "\\d{5}-\\d{3}";
            return cep.matches(regex);
        }
    }
}

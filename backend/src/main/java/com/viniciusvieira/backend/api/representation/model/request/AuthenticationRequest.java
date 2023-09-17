package com.viniciusvieira.backend.api.representation.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {
    @NotBlank(message = "EMAIL não pode ser nulo ou estar em branco")
    private String email;

    @NotBlank(message = "PASSWORD não pode ser nulo ou estar em branco")
    private String password;
}

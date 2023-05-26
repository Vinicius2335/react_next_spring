package com.viniciusvieira.backend.api.representation.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class EstadoRequest {
    @NotBlank(message = "Nome não pode ser nulo ou vazio")
    private String nome;

    @NotBlank(message = "Sigla não pode ser nulo ou vazio")
    private String sigla;
}

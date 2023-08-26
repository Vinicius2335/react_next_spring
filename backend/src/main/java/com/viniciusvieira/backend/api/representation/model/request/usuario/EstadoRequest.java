package com.viniciusvieira.backend.api.representation.model.request.usuario;

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
    @NotBlank(message = "NOME não pode ser nulo ou vazio")
    private String nome;

    @NotBlank(message = "SIGLA não pode ser nulo ou vazio")
    private String sigla;
}

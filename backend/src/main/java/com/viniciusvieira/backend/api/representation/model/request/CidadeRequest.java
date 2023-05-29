package com.viniciusvieira.backend.api.representation.model.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CidadeRequest {
    @NotBlank(message = "Nome não pode ser nulo ou vazio")
    private String nome;

    @Valid
    @NotNull
    private EstadoIdRequest estadoId;
}

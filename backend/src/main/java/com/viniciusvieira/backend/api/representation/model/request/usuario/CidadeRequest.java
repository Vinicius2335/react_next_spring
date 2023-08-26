package com.viniciusvieira.backend.api.representation.model.request.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CidadeRequest {
    @NotBlank(message = "NOME não pode ser nulo ou vazio")
    private String nome;

    @NotNull(message = "ESTADO_ID  não pode ser nulo")
    @Positive(message = "ESTADO_ID não pode ser negativo ou zero")
    private Long estadoId;
}

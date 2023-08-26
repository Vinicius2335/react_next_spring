package com.viniciusvieira.backend.api.representation.model.request.venda;

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
public class CarrinhoDeCompraRequest {
    @NotBlank(message = "SITUAÇÃO não pode ser nulo ou estar em branco")
    private String situacao;

    @NotBlank(message = "OBSERVAÇÃO não pode ser nulo ou estar em branco")
    private String observacao;

    @NotNull(message = "PESSOA_ID não pode ser nulo")
    @Positive(message = "PESSOA_ID não pode ser negatiovo ou zero")
    private Long pessoaId;
}

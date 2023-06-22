package com.viniciusvieira.backend.api.representation.model.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CarrinhoDeCompraProdutoRequest {
    @NotNull(message = "VALOR não pode ser nulo")
    @Positive(message = "VALOR não pode ser negativo")
    private BigDecimal valor;
    @Positive(message = "QUANTIDADE não pode ser negativo")
    private int quantidade;
    // pode ser vazio ou nulo
    private String observacao;
    @Valid
    private ProdutoIdRequest produto;
    @Valid
    private CarrinhoDeCompraIdRequest carrinhoCompra;
}

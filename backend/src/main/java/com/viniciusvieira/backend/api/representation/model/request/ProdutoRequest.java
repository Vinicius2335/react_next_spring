package com.viniciusvieira.backend.api.representation.model.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ProdutoRequest {
    @NotNull(message = "Quantidade não pode ser nulo")
    @Min(value = 0, message = "Quantidade minima tem que ser maior que 0")
    private Integer quantidade;

    @NotBlank(message = "Descrição Curta não pode ser nulo")
    private String descricaoCurta;

    @NotBlank(message = "Descrição Detalhada não pode ser nulo")
    private String descricaoDetalhada;

    @NotNull(message = "Valor de Custo não pode ser nulo")
    @Positive(message = "Valor de Custo não pode ser negativo")
    private BigDecimal valorCusto;

    @NotNull(message = "Valor de Venda não pode ser nulo")
    @Positive(message = "Valor de Venda não pode ser negativo")
    private BigDecimal valorVenda;

    @Valid
    @NotNull(message = "Id Marca não pode ser nulo")
    private MarcaIdRequest marca;

    @Valid
    @NotNull(message = "Id Categoria não pode ser nulo")
    private CategoriaIdRequest categoria;
}

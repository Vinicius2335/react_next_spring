package com.viniciusvieira.backend.api.representation.model.response.venda;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CarrinhoDeCompraProdutoResponse {
    private BigDecimal valor;
    private int quantidade;
    private String observacao;
    private OffsetDateTime dataCriacao;
    private OffsetDateTime dataAtualizacao;
}

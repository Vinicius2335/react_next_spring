package com.viniciusvieira.backend.api.representation.model.response.venda;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProdutoResponse {
    private Integer quantidade;
    private String descricaoCurta;
    private String descricaoDetalhada;
    private BigDecimal valorCusto;
    private BigDecimal valorVenda;
    private OffsetDateTime dataCriacao;
    private OffsetDateTime dataAtualizacao;
    private MarcaResponse marca;
    private CategoriaResponse categoria;
}

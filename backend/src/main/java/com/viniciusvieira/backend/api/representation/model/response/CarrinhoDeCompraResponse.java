package com.viniciusvieira.backend.api.representation.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CarrinhoDeCompraResponse {
    private String situacao;
    private String observacao;
    private PessoaResponse pessoa;
    private OffsetDateTime dataCompra;
    private OffsetDateTime dataAtualizacao;
}
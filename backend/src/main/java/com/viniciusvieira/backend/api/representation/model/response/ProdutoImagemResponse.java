package com.viniciusvieira.backend.api.representation.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.OffsetDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProdutoImagemResponse {
    private String nome;
    private String imageCode;
    private OffsetDateTime dataCriacao;
    private OffsetDateTime dataAtualizacao;
}

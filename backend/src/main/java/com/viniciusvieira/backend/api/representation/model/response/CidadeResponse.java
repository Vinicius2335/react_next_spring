package com.viniciusvieira.backend.api.representation.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CidadeResponse {
    private String nome;
    private String nomeEstado;
    private OffsetDateTime dataCriacao;
    private OffsetDateTime dataAtualizacao;
}

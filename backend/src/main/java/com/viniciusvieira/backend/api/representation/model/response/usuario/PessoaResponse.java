package com.viniciusvieira.backend.api.representation.model.response.usuario;

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
public class PessoaResponse {
    private String nome;
    private String cpf;
    private String email;
    private String senha;

    private EnderecoResponse endereco;

    private OffsetDateTime dataCriacao;
    private OffsetDateTime dataAtualizacao;
}

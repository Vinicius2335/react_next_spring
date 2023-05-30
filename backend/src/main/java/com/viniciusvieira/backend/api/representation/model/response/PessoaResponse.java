package com.viniciusvieira.backend.api.representation.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.viniciusvieira.backend.api.representation.model.request.CidadeIdRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    private String endereco;
    private String cep;
    private CidadeResponse cidade;
    private OffsetDateTime dataCriacao;
    private OffsetDateTime dataAtualizacao;
}

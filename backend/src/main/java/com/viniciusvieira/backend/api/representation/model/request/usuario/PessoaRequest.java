package com.viniciusvieira.backend.api.representation.model.request.usuario;

import com.viniciusvieira.backend.core.validation.CepValidation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PessoaRequest {
    @NotBlank(message = "NOME não pode ser nulo ou em branco")
    private String nome;

    @NotBlank(message = "CPF não pode ser nulo ou em branco")
    @CPF
    private String cpf;

    @NotBlank(message = "EMAIL não pode ser nulo ou em branco")
    @Email
    private String email;

    @NotBlank(message = "SENHA não pode ser nulo ou em branco")
    private String senha;

    @NotBlank(message = "ENDERECO não pode ser nulo ou em branco")
    private String endereco;

    @NotBlank(message = "CEP não pode ser nulo ou em branco")
    @CepValidation
    private String cep;

    @NotBlank(message = "Nome da Permissao nao pode ser nulo ou em branco")
    private String nomePermissao;

    @Valid
    @NotNull
    private CidadeIdRequest cidade;
}

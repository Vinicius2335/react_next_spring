package com.viniciusvieira.backend.api.representation.model.request;

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
    @NotBlank(message = "NOME não pode ser nulo ou branco")
    private String nome;

    // TODO - talvez uma anotaçao personalizada
    @NotBlank(message = "CPF não pode ser nulo ou branco")
    @CPF
    private String cpf;

    @NotBlank(message = "EMAIL não pode ser nulo ou branco")
    @Email
    private String email;

    @NotBlank(message = "SENHA não pode ser nulo ou branco")
    private String senha;

    @NotBlank(message = "ENDERECO não pode ser nulo ou branco")
    private String endereco;

    // TODO - talvez uma anotaçao personalizada
    @NotBlank(message = "CEP não pode ser nulo ou branco")
    @CepValidation
    private String cep;

    @Valid
    @NotNull
    private CidadeIdRequest cidade;
}

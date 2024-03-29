package com.viniciusvieira.backend.api.representation.model.request.usuario;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ClienteRequest {
    @NotBlank(message = "NOME não pode ser nulo ou em branco")
    private String nome;

    @NotBlank(message = "CPF não pode ser nulo ou em branco")
    @CPF
    private String cpf;

    @NotBlank(message = "EMAIL não pode ser nulo ou em branco")
    @Email
    private String email;

   @Valid
   private EnderecoRequest endereco;
}

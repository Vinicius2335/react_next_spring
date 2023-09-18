package com.viniciusvieira.backend.api.representation.model.request.usuario;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PessoaGerenciamentoRequest {
    @NotBlank(message = "EMAIL não pode ser nulo ou vazio")
    private String email;

    @NotBlank(message = "CODIGO não pode ser nulo ou vazio")
    private String codigoParaRecuperarSenha;

    @NotBlank(message = "SENHA não pode ser nulo ou vazio")
    private String senha;
}

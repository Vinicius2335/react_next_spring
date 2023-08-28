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
public class EnderecoRequest {
    @NotBlank(message = "ENDEREÇO não pode ser nulo ou estar em branco")
    private String logradouro;

    @NotBlank(message = "CIDADE não pode ser nulo ou estar em branco")
    private String cidade;

    @NotBlank(message = "ESTADO não pode ser nulo ou estar em branco")
    private String estado;

    @NotBlank(message = "CEP não pode ser nulo ou estar em branco")
    private String cep;
}

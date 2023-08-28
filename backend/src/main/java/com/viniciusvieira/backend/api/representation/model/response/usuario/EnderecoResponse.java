package com.viniciusvieira.backend.api.representation.model.response.usuario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class EnderecoResponse {
    private String nomeEndereco;
    private String cidade;
    private String estado;
    private String cep;
}

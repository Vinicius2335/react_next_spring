package com.viniciusvieira.backend.domain.model.usuario;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Embeddable
public class Endereco {
    @Column(name = "endereco_nome", nullable = false)
    private String nome;

    @Column(name = "endereco_estado", nullable = false)
    private String estado;

    @Column(name = "endereco_cidade", nullable = false)
    private String cidade;

    @Column(name = "endereco_cep", nullable = false)
    private String cep;
}

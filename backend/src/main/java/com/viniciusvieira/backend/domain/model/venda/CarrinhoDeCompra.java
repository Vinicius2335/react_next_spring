package com.viniciusvieira.backend.domain.model.venda;

import com.viniciusvieira.backend.domain.model.BaseEntity;
import com.viniciusvieira.backend.domain.model.usuario.Pessoa;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
@Entity
@Table(name = "carrinho_compra")
public class CarrinhoDeCompra extends BaseEntity {
    @Column(nullable = false)
    private String situacao;

    @Column(nullable = false)
    private String observacao;

    // 1 pessoa possue 0 ou * carrinho de compra
    @ManyToOne
    @JoinColumn(name = "pessoa_id")
    private Pessoa pessoa;
}

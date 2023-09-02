package com.viniciusvieira.backend.domain.model.venda;

import com.viniciusvieira.backend.domain.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
@Entity
@Table(name = "carrinho_compra_produto")
public class CarrinhoDeCompraProduto extends BaseEntity {
    @Column(nullable = false)
    private BigDecimal valor;

    @Column(nullable = false)
    private int quantidade;

    @Column(columnDefinition = "TEXT")
    private String observacao;

    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @ManyToOne
    @JoinColumn(name = "carrinho_compra_id", nullable = false)
    private CarrinhoDeCompra carrinhoDeCompra;

}

package com.viniciusvieira.backend.domain.model.venda;

import com.viniciusvieira.backend.domain.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
@Entity
@Table(name = "produto")
@EqualsAndHashCode(callSuper = true)
public class Produto extends BaseEntity {
    @Column(nullable = false)
    private int quantidade;

    @Column(nullable = false)
    private String descricaoCurta;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descricaoDetalhada;

    @Column(nullable = false)
    private BigDecimal valorCusto;

    @Column(nullable = false)
    private BigDecimal valorVenda;

    @ManyToOne
    @JoinColumn(name = "marca_id", nullable = false)
    private Marca marca;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

}

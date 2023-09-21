package com.viniciusvieira.backend.domain.model.venda;

import com.viniciusvieira.backend.domain.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
@Entity
@Table(name = "imagens")
@EqualsAndHashCode(callSuper = true)
public class ProdutoImagem extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String nome;

    private String imageCode;
    @Transient
    private byte[] arquivo;

    @ManyToOne
    @JoinColumn(nullable = false, name = "produto_id")
    private Produto produto;
}

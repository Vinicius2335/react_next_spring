package com.viniciusvieira.backend.domain.model.venda;

import com.viniciusvieira.backend.domain.model.BaseEntity;
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
@Table(name = "imagens")
public class ProdutoImagem extends BaseEntity {
    @Column(nullable = false)
    private String nome;

    @ManyToOne
    @JoinColumn(nullable = false, name = "produto_id")
    private Produto produto;
}

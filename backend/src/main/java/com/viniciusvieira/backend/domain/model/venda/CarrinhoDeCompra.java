package com.viniciusvieira.backend.domain.model.venda;

import com.viniciusvieira.backend.domain.model.usuario.Pessoa;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "carrinho_compra")
public class CarrinhoDeCompra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String situacao;

    @Column(nullable = false)
    private String observacao;

    @CreationTimestamp
    private OffsetDateTime dataCompra;

    @UpdateTimestamp
    private OffsetDateTime dataAtualizacao;

    // 1 pessoa possue 0 ou * carrinho de compra
    @ManyToOne
    @JoinColumn(name = "pessoa_id")
    private Pessoa pessoa;
}

package com.viniciusvieira.backend.domain.model;

import com.viniciusvieira.backend.domain.exception.PermissaoNaoEncontradaException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "pessoa")
public class Pessoa {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String cpf;

    @Column(nullable = false)
    private String email;

    private String senha;

    @Column(nullable = false)
    private String endereco;

    @Column(nullable = false)
    private String cep;

    private String codigoRecuperacaoSenha;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataEnvioCodigo;

    @CreationTimestamp
    private OffsetDateTime dataCriacao;

    @UpdateTimestamp
    private OffsetDateTime dataAtualizacao;

    @ManyToOne
    @JoinColumn(name = "cidade_id")
    private Cidade cidade;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "pessoa_permissao",
            joinColumns = @JoinColumn(name = "id_pessoa"),
            inverseJoinColumns = @JoinColumn(name = "id_permissao")
    )
    private List<Permissao> permissoes = new ArrayList<>();

    public void adicionarPermissao(Permissao permissao){
        this.permissoes.add(permissao);
        permissao.getPessoas().add(this);
    }

    public void removerPermissao(Long idPermissao){
        Permissao permissaoParaRemover = this.permissoes.stream()
                .filter(permissao -> Objects.equals(permissao.getId(), idPermissao))
                .findFirst()
                .orElseThrow(() -> new PermissaoNaoEncontradaException("Permissao nao cadastrada ou nao registrada para essa pessoa"));
        this.permissoes.remove(permissaoParaRemover);
        permissaoParaRemover.getPessoas().remove(this);
    }
}

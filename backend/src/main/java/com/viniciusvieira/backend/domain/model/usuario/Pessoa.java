package com.viniciusvieira.backend.domain.model.usuario;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.viniciusvieira.backend.domain.exception.PermissaoNaoEncontradaException;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String cpf;

    @Column(nullable = false)
    private String email;

    private String senha;

    private String codigoRecuperacaoSenha;

    private LocalDateTime dataEnvioCodigo;

    @Embedded
    private Endereco endereco;

    @CreationTimestamp
    private OffsetDateTime dataCriacao;

    @UpdateTimestamp
    private OffsetDateTime dataAtualizacao;

    // TEST - Campo agora é final
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "pessoa_permissao",
            joinColumns = @JoinColumn(name = "id_pessoa"),
            inverseJoinColumns = @JoinColumn(name = "id_permissao")
    )
    @Setter(AccessLevel.NONE)
    @JsonIgnore
    private List<Permissao> permissoes = new ArrayList<>();

    public void adicionarPermissao(Permissao permissao){
        this.getPermissoes().add(permissao);
        permissao.getPessoas().add(this);
    }

    public void removerPermissao(Long idPermissao){
        Permissao permissaoParaRemover = this.permissoes.stream()
                .filter(permissao -> Objects.equals(permissao.getId(), idPermissao))
                .findFirst()
                .orElseThrow(() -> new PermissaoNaoEncontradaException("Permissao nao cadastrada ou nao registrada para essa pessoa"));
        this.getPermissoes().remove(permissaoParaRemover);
        permissaoParaRemover.getPessoas().remove(this);
    }
}

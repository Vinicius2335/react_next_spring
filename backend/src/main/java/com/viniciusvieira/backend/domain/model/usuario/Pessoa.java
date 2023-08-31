package com.viniciusvieira.backend.domain.model.usuario;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.viniciusvieira.backend.domain.exception.usuario.PermissaoNaoEncontradaException;
import com.viniciusvieira.backend.domain.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
@Entity
@Table(name = "pessoa")
public class Pessoa extends BaseEntity {

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

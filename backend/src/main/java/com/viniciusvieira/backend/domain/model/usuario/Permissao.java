package com.viniciusvieira.backend.domain.model.usuario;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "permissao")
public class Permissao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @CreationTimestamp
    private OffsetDateTime dataCriacao;

    @UpdateTimestamp
    private OffsetDateTime dataAtualizacao;

    @ManyToMany(
            mappedBy = "permissoes",
            cascade = {CascadeType.MERGE, CascadeType.PERSIST},
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    @Setter(AccessLevel.NONE)
    @ToString.Exclude
    private List<Pessoa> pessoas = new ArrayList<>();

    // TEST
    public void addNewPessoa(Pessoa pessoa){
        this.getPessoas().add(pessoa);
        pessoa.getPermissoes().add(this);
    }

    // TEST - verificar se o removeIf funciona como eu quero
    public void removePessoa(Pessoa pessoa){
        getPessoas().removeIf(pessoa1 -> pessoa1.getId().equals(pessoa.getId()));
        pessoa.getPermissoes().remove(this);
    }
}

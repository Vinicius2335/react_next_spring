package com.viniciusvieira.backend.domain.model.usuario;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.viniciusvieira.backend.domain.exception.usuario.PermissaoNaoEncontradaException;
import com.viniciusvieira.backend.domain.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
@Entity
@Table(name = "pessoa")
@EqualsAndHashCode(callSuper = true)
public class Pessoa extends BaseEntity implements UserDetails {

    @Serial
    private static final long serialVersionUID = -8223683363491564192L;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String cpf;

    @Column(nullable = false, unique = true)
    private String email;

    private String senha;

    private String codigoRecuperacaoSenha;

    private LocalDateTime dataEnvioCodigo;

    @Embedded
    private Endereco endereco;


    @JsonIgnore
    @Builder.Default
    @Setter(AccessLevel.NONE)
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "pessoa_permissao",
            joinColumns = @JoinColumn(name = "id_pessoa"),
            inverseJoinColumns = @JoinColumn(name = "id_permissao")
    )
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

    public String getRolesString(){
        return getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(", "));

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        this.permissoes.forEach(permissao ->
                authorities.add(new SimpleGrantedAuthority("ROLE_" + permissao.getNome().toUpperCase()))
        );

        return authorities;
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

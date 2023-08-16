package com.viniciusvieira.backend.domain.repository.usuario;

import com.viniciusvieira.backend.domain.model.usuario.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadoRepository extends JpaRepository<Estado, Long> {

}

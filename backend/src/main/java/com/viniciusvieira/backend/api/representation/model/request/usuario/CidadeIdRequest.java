package com.viniciusvieira.backend.api.representation.model.request.usuario;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CidadeIdRequest {
    @NotNull(message = "Id de cidade não pode ser nulo")
    private Long id;
}

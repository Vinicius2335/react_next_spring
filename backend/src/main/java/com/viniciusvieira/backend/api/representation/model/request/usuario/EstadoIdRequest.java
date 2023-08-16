package com.viniciusvieira.backend.api.representation.model.request.usuario;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EstadoIdRequest {
    @NotNull(message = "Id de Estado não pode ser nulo")
    private Long id;
}

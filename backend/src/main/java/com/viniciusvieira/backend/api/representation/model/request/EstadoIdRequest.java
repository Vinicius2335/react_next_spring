package com.viniciusvieira.backend.api.representation.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EstadoIdRequest {
    @NotNull(message = "Id de Estado não pode ser nulo")
    private Long id;
}

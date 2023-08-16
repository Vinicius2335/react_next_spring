package com.viniciusvieira.backend.api.representation.model.request.venda;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CategoriaIdRequest {
    @NotNull
    private Long id;
}

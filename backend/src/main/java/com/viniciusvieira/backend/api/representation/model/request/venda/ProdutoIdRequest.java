package com.viniciusvieira.backend.api.representation.model.request.venda;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class ProdutoIdRequest {
    @NotNull(message = "ID de produto não pode ser nulo")
    private Long id;
}

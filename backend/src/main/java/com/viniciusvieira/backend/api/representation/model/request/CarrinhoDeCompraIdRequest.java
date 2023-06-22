package com.viniciusvieira.backend.api.representation.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class CarrinhoDeCompraIdRequest {
    @NotNull(message = "ID Carrinho de compra não pode ser nulo")
    private Long id;
}

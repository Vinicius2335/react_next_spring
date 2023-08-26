package com.viniciusvieira.backend.api.representation.model.request.venda.ids;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoriaId {
    @NotNull(message = "CATEGORIA_ID não pode ser nulo")
    @Positive(message = "CATEGORIA_ID não pode ser negatiovo ou zero")
    private Long id;

}

package com.viniciusvieira.backend.api.representation.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PessoaIdRequest {
    @NotNull
    private Long id;
}

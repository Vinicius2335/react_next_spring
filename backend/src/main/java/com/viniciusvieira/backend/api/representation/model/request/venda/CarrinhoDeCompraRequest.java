package com.viniciusvieira.backend.api.representation.model.request.venda;

import com.viniciusvieira.backend.api.representation.model.request.usuario.PessoaIdRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CarrinhoDeCompraRequest {
    @NotBlank(message = "SITUAÇÃO não pode ser nulo ou estar em branco")
    private String situacao;

    @NotBlank(message = "OBSERVAÇÃO não pode ser nulo ou estar em branco")
    private String observacao;

    @Valid
    @NotNull(message = "ID de pessoa é obrigatório")
    private PessoaIdRequest pessoa;
}

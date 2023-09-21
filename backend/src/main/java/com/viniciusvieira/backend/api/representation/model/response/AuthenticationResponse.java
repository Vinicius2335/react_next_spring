package com.viniciusvieira.backend.api.representation.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.viniciusvieira.backend.api.representation.model.response.usuario.PessoaResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("refresh_token")
    private String refreshToken;

    private PessoaResponse user;

    private String roles;
}

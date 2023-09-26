package com.viniciusvieira.backend.util;

import com.viniciusvieira.backend.domain.model.token.TokenModel;
import com.viniciusvieira.backend.domain.model.usuario.Pessoa;

public abstract class TokenCreator {
    public static TokenModel crateToken(Pessoa pessoa){
        return TokenModel.builder()
                .pessoa(pessoa)
                .token("eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6IlJPTEVfREFMRSIsInN1YiI6ImdvdWxhcnQyMzM1QGdtYWlsLmNvbSIsImlhdCI6MTY5NTUwODE4NSwiZXhwIjoxNjk1NTk0NTg1fQ.GcLIRovOBGMwjQ3bjhGInaybPEQZFe_O2zZuZPSndA4")
                .revoked(false)
                .expired(false)
                .build();
    }
}

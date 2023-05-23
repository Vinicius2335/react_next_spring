package com.viniciusvieira.backend.domain.service;


import java.util.List;

public interface ICrud <T, ID> {
    List<T> buscarTodos();

    T buscarPeloId(ID id);

    T inserir(T entity);

    T alterar(ID id, T entity);

    void excluir(ID id);
}

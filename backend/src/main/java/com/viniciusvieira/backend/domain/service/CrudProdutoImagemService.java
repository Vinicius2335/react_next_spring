package com.viniciusvieira.backend.domain.service;

import com.viniciusvieira.backend.api.mapper.ProdutoImagemMapper;
import com.viniciusvieira.backend.api.representation.model.response.ProdutoImagemResponse;
import com.viniciusvieira.backend.domain.exception.NegocioException;
import com.viniciusvieira.backend.domain.exception.ProdutoImagemNaoEncontradoException;
import com.viniciusvieira.backend.domain.model.Produto;
import com.viniciusvieira.backend.domain.model.ProdutoImagem;
import com.viniciusvieira.backend.domain.repository.ProdutoImagemRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@AllArgsConstructor
@Service
public class CrudProdutoImagemService {
    private final ProdutoImagemRepository produtoImagemRepository;
    private final ProdutoImagemMapper produtoImagemMapper;
    private static final String PATH_DIRECTORY = "src/main/resources/static/image";

    public List<ProdutoImagem> buscarTodos() {
        return produtoImagemRepository.findAll();
    }

    public ProdutoImagem buscarPorId(Long id){
        return produtoImagemRepository.findById(id)
                .orElseThrow(() -> new ProdutoImagemNaoEncontradoException("Imagem não encontrada"));
    }

    @Transactional
    public ProdutoImagemResponse inserir(Produto produto, String nomeImagem){
        ProdutoImagem produtoImagem = ProdutoImagem.builder()
                .nome(nomeImagem)
                .produto(produto)
                .build();
        ProdutoImagem imagemSalva = produtoImagemRepository.saveAndFlush(produtoImagem);

        return produtoImagemMapper.toProdutoImagemResponse(imagemSalva);
    }

    @Transactional
    public ProdutoImagemResponse alterar(Long id, String nomeImagem){
        ProdutoImagem produtoImagemParaAlterar = buscarPorId(id);
        Path diretorioDeImagens = Paths.get(PATH_DIRECTORY);
        boolean isImageDeleted = deletandoImagemArmazenada(produtoImagemParaAlterar, diretorioDeImagens);

        if (isImageDeleted){
            produtoImagemParaAlterar.setNome(nomeImagem);
            ProdutoImagem imagemAlterada = produtoImagemRepository.saveAndFlush(produtoImagemParaAlterar);
            return produtoImagemMapper.toProdutoImagemResponse(imagemAlterada);
        } else {
           throw new NegocioException("Erro ao tentar alterar a imagem do produto");
        }
    }

    @Transactional
    public void excluir(Long id) {
        ProdutoImagem produtoImagem = buscarPorId(id);
        Path diretorioDeImagens = Paths.get(PATH_DIRECTORY);
        boolean isImageDeleted = deletandoImagemArmazenada(produtoImagem, diretorioDeImagens);

        if (isImageDeleted){
            produtoImagemRepository.delete(produtoImagem);
        } else {
            throw new NegocioException("Erro ao tentar excluir a imagem do produto");
        }
    }

    private boolean deletandoImagemArmazenada(ProdutoImagem produtoImagem, Path diretorioDeImagens){
        try(DirectoryStream<Path> stream = Files.newDirectoryStream(diretorioDeImagens)){
            for (Path path : stream){
                if (path.getFileName().toString().equals(produtoImagem.getNome())){
                    return Files.deleteIfExists(path);
                }
            }
            return false;
        } catch (IOException e) {
            throw new NegocioException("Erro ao tentar encontrar o arquivo da imagem", e);
        }
    }

}

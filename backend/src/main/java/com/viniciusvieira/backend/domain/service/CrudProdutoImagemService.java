package com.viniciusvieira.backend.domain.service;

import com.viniciusvieira.backend.api.mapper.ProdutoImagemMapper;
import com.viniciusvieira.backend.api.representation.model.response.ProdutoImagemResponse;
import com.viniciusvieira.backend.domain.exception.NegocioException;
import com.viniciusvieira.backend.domain.exception.ProdutoImagemNaoEncontradoException;
import com.viniciusvieira.backend.domain.model.venda.Produto;
import com.viniciusvieira.backend.domain.model.venda.ProdutoImagem;
import com.viniciusvieira.backend.domain.repository.venda.ProdutoImagemRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
    public static final String PATH_DIRECTORY = "src/main/resources/static/image";

    public List<ProdutoImagem> buscarPorProduto(Long idProduto){
        List<ProdutoImagem> listaImagens = produtoImagemRepository.findByProdutoId(idProduto);

        listaImagens.forEach(produtoImagem -> {
            try (InputStream in = new FileInputStream(PATH_DIRECTORY + "/" +produtoImagem.getNome())){
                //IOUtils - Do Commons IO
                produtoImagem.setArquivo(IOUtils.toByteArray(in));
            } catch (IOException e) {
                throw new NegocioException("Erro ao tentar buscar imagem pelo id do produto: " +idProduto ,e);
            }
        });

        return listaImagens;
    }

    public ProdutoImagem buscarPorId(Long id){
        return produtoImagemRepository.findById(id)
                .orElseThrow(() -> new ProdutoImagemNaoEncontradoException("Imagem não encontrada"));
    }

    @Transactional
    public ProdutoImagemResponse inserir(Produto produto, String nomeImagem, String fileCode){
        ProdutoImagem produtoImagem = ProdutoImagem.builder()
                .nome(nomeImagem)
                .produto(produto)
                .imageCode(fileCode)
                .build();
        ProdutoImagem imagemSalva = produtoImagemRepository.saveAndFlush(produtoImagem);

        return produtoImagemMapper.toProdutoImagemResponse(imagemSalva);
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
        try(DirectoryStream<Path> directoryStream = Files.newDirectoryStream(diretorioDeImagens)){
            for (Path path : directoryStream){
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

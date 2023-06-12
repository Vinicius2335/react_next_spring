package com.viniciusvieira.backend.domain.service;

import com.viniciusvieira.backend.api.representation.model.response.ProdutoImagemResponse;
import com.viniciusvieira.backend.domain.model.Produto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@RequiredArgsConstructor
@Service
public class ImagemUploadService {
    private final CrudProdutoImagemService crudProdutoImagemService;


    // NOTE - Atenção se o arquivo estiver vazio
    public ProdutoImagemResponse uploadEInseriNovaImagem(Produto produto, MultipartFile file) throws IOException {
        try {
            if (!file.isEmpty()){
                String fileName = uploadImagem(file);
                return crudProdutoImagemService.inserir(produto, fileName);
            }
            return null;

        } catch (IOException e) {
            throw new IOException("Error ao tentar realizar o upload/salvar nova imagem. . .");
        }
    }

    public ProdutoImagemResponse uploadEAlteraImagem(Long idImagem, MultipartFile file) throws IOException {
        try {
            if (!file.isEmpty()){
                String nomeImagem = uploadImagem(file);
                return  crudProdutoImagemService.alterar(idImagem, nomeImagem);
            }
            return null;

        } catch (IOException e) {
            throw new IOException("Error ao tentar realizar o upload/alterar imagem. . .");
        }
    }

    private String uploadImagem(MultipartFile file)
            throws IOException {
        String pathDirectory = "src/main/resources/static/image";
        String fileCode = generateRandomCode();
        String fileName = fileCode + "-" + file.getOriginalFilename();

        Files.copy(
                file.getInputStream(),
                Paths.get(pathDirectory + File.separator + fileName),
                StandardCopyOption.REPLACE_EXISTING
        );

        return fileName;
    }

    private String generateRandomCode(){
        return RandomStringUtils.randomAlphabetic(8);
    }

}

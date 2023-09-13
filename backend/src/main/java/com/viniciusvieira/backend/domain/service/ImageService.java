package com.viniciusvieira.backend.domain.service;

import com.viniciusvieira.backend.api.representation.model.response.ProdutoImagemResponse;
import com.viniciusvieira.backend.domain.model.venda.Produto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import static com.viniciusvieira.backend.domain.service.CrudProdutoImagemService.PATH_DIRECTORY;

@RequiredArgsConstructor
@Service
public class ImageService {
    private final CrudProdutoImagemService crudProdutoImagemService;
    private Path foundFile;

    public Resource dowloadImage(String imageCode) throws IOException {
        Path uploadDirector = Path.of(PATH_DIRECTORY);

        try (Stream<Path> files = Files.list(uploadDirector)) {
            files.forEach(file -> {
                if (file.getFileName().toString().startsWith(imageCode)) {
                    foundFile = file;
                    return;
                }
            });
        } catch (IOException e) {
            throw new IOException("Error while try found files ", e);
        }

        if (foundFile != null) {
            return new UrlResource(foundFile.toUri());
        }

        return null;
    }

    // COMMENT - Atenção se o arquivo estiver vazio
    public ProdutoImagemResponse uploadEInseriNovaImagem(Produto produto, MultipartFile file) throws IOException {
        try {
            if (!file.isEmpty()){
                String fileCode = generateRandomCode();
                String fileName = uploadImagem(file, fileCode);
                return crudProdutoImagemService.inserir(produto, fileName, fileCode);
            }
            return null;

        } catch (IOException e) {
            throw new IOException("Error ao tentar realizar o upload/salvar nova imagem. . .");
        }
    }

    // TODO - Remover o alterar
    public ProdutoImagemResponse uploadEAlteraImagem(Long idImagem, MultipartFile file) throws IOException {
        try {
            if (!file.isEmpty()){
                String fileCode = generateRandomCode();
                String nomeImagem = uploadImagem(file, fileCode);
                return  crudProdutoImagemService.alterar(idImagem, nomeImagem, fileCode);
            }
            return null;

        } catch (IOException e) {
            throw new IOException("Error ao tentar realizar o upload/alterar imagem. . .");
        }
    }

    private String uploadImagem(MultipartFile file, String fileCode)
            throws IOException {
        String pathDirectory = "src/main/resources/static/image";
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

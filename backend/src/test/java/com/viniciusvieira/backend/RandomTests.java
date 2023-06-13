package com.viniciusvieira.backend;

import com.viniciusvieira.backend.domain.exception.NegocioException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class RandomTests {
    private static final String PATH_DIRECTORY = "src/main/resources/static/image";

    @Test
    void testeLocalizarArquivoPeloNome(){
        assertDoesNotThrow(() -> {
            Path diretorioDeImagens = Paths.get(PATH_DIRECTORY);
            try(DirectoryStream<Path> stream = Files.newDirectoryStream(diretorioDeImagens)){
                for (Path path : stream){
                    if (path.getFileName().toString().equals("AlzTXwFH-escudo.jpg")){
                        System.out.println("Arquivo encontrado pelo nome com sucesso!!");
                    }
//                System.out.println(path.getFileName());
                }
            } catch (IOException e) {
                throw new NegocioException("Erro ao tentar encontrar o arquivo da imagem", e);
            }
        });
    }
}

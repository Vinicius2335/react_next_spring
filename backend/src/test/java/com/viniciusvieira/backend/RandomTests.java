package com.viniciusvieira.backend;

import com.viniciusvieira.backend.domain.exception.NegocioException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

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
                // System.out.println(path.getFileName());
                }
            } catch (IOException e) {
                throw new NegocioException("Erro ao tentar encontrar o arquivo da imagem", e);
            }
        });
    }

    @Test
    void TesteData(){
        assertDoesNotThrow(() -> {
            Date data = new Date();
            System.out.println("Data antes de adicionar 15 min: " + data);
            data.setMinutes(data.getMinutes() + 15);
            System.out.println("Data depois de adicionar 15 min: " + data);
        });
    }

    @Test
    void TesteData2(){
        assertDoesNotThrow(() -> {
            Date data = new Date();
            Date data2 = new Date();
            data2.setMinutes(data2.getMinutes() + 17);

            LocalDateTime localDateTime = convertToLocalDateTime(data);
            LocalDateTime localDateTime2 = convertToLocalDateTime(data2);

            System.out.println(ChronoUnit.MINUTES.between(localDateTime, localDateTime2));
        });
    }

    private LocalDateTime convertToLocalDateTime(Date date){
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    private LocalDate convertToLocalDate(Date date){
        return LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }
}

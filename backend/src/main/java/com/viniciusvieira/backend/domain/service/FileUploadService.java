package com.viniciusvieira.backend.domain.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileUploadService {

    public String uploadFilePathDirectory(MultipartFile file) throws IOException {
        try {
            String path = "src/main/resources/static/image";
            return upload(path, file);
        } catch (IOException e) {
            throw new IOException("Error saving upload file. . .");
        }
    }

    private String upload(String path, MultipartFile file) throws IOException {
        String fileCode = generateRandomCode();
        String fileName = fileCode + "-" + file.getOriginalFilename();

        Files.copy(
                file.getInputStream(),
                Paths.get(path + File.separator + fileName),
                StandardCopyOption.REPLACE_EXISTING
        );

        return "Upload Realizado Com Sucesso...";
    }

    private String generateRandomCode(){
        return RandomStringUtils.randomAlphabetic(8);
    }
}

package com.viniciusvieira.backend.api.controller;

import com.viniciusvieira.backend.domain.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/upload")
public class UploadController {
    private final FileUploadService fileUploadService;

    // TODO - retornar um fileResponse
    @PostMapping
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        String fileSaved = fileUploadService.uploadFilePathDirectory(file);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(fileSaved);
    }
}

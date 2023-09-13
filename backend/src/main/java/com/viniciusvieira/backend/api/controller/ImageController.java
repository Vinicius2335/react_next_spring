package com.viniciusvieira.backend.api.controller;

import com.viniciusvieira.backend.domain.model.venda.ProdutoImagem;
import com.viniciusvieira.backend.domain.service.CrudProdutoImagemService;
import com.viniciusvieira.backend.domain.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/imagens")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ImageController {
    private final CrudProdutoImagemService crudProdutoImagemService;
    private final ImageService imageService;

    @GetMapping
    public ResponseEntity<List<ProdutoImagem>> buscarTodos(){
        return ResponseEntity
                .ok(crudProdutoImagemService.buscarTodos());
    }

    @GetMapping("/{fileCode}")
    public ResponseEntity<?> dowloadImage(@PathVariable("fileCode") String fileCode){
        Resource resource;

        try {
            resource = imageService.dowloadImage(fileCode);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }

        if (resource == null) {
            return new ResponseEntity<>("Imagem não cadastrada", HttpStatus.NOT_FOUND);
        }

        String contentTypeImage = "image/jpeg";
        String headerValue = "attachment; filename=\"" + resource.getFilename() + "\"";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentTypeImage))
                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                .body(resource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        crudProdutoImagemService.excluir(id);
        return ResponseEntity
                .noContent()
                .build();
    }
}

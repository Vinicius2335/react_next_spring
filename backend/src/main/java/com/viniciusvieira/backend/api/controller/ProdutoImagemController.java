package com.viniciusvieira.backend.api.controller;

import com.viniciusvieira.backend.api.representation.model.response.ProdutoImagemResponse;
import com.viniciusvieira.backend.domain.model.venda.Produto;
import com.viniciusvieira.backend.domain.model.venda.ProdutoImagem;
import com.viniciusvieira.backend.domain.service.CrudProdutoImagemService;
import com.viniciusvieira.backend.domain.service.ImageService;
import com.viniciusvieira.backend.domain.service.venda.CrudProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/produtos/{idProduto}/imagens")
@RequiredArgsConstructor
public class ProdutoImagemController {
    private final ImageService imageService;
    private final CrudProdutoService crudProdutoService;
    private final CrudProdutoImagemService crudProdutoImagemService;

    @GetMapping
    public ResponseEntity<List<ProdutoImagem>> buscarImagensPorProduto(@PathVariable Long idProduto){
        return ResponseEntity
                .ok(crudProdutoImagemService.buscarPorProduto(idProduto));
    }

    @PostMapping
    public ResponseEntity<ProdutoImagemResponse> uploadFile(
            @PathVariable Long idProduto,
            @RequestParam("file") MultipartFile file
    ) throws IOException {

        Produto produtoEncontrado = crudProdutoService.buscarPorId(idProduto);
        ProdutoImagemResponse imagemSalva = imageService.uploadEInseriNovaImagem(produtoEncontrado, file);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(imagemSalva);
    }
}

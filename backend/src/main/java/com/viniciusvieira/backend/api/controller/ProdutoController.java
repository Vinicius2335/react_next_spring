package com.viniciusvieira.backend.api.controller;

import com.viniciusvieira.backend.api.representation.model.request.ProdutoRequest;
import com.viniciusvieira.backend.api.representation.model.response.ProdutoImagemResponse;
import com.viniciusvieira.backend.api.representation.model.response.ProdutoResponse;
import com.viniciusvieira.backend.domain.model.Produto;
import com.viniciusvieira.backend.domain.service.CrudProdutoService;
import com.viniciusvieira.backend.domain.service.ImagemUploadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {
    private final CrudProdutoService crudProdutoService;
    private final ImagemUploadService imagemUploadService;

    @GetMapping
    public ResponseEntity<List<Produto>> buscarTodos(){
        return ResponseEntity.ok(crudProdutoService.buscarTodos());
    }

    @PostMapping
    public ResponseEntity<ProdutoResponse> inserir(@RequestBody @Valid ProdutoRequest produtoRequest){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(crudProdutoService.inserir(produtoRequest));
    }

    @PostMapping("/{idProduto}/image/")
    public ResponseEntity<ProdutoImagemResponse> uploadFile(@PathVariable Long idProduto, @RequestParam("file") MultipartFile file) throws IOException {
        Produto produtoEncontrado = crudProdutoService.buscarPorId(idProduto);
        ProdutoImagemResponse imagemSalva = imagemUploadService.uploadEInseriNovaImagem(produtoEncontrado, file);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(imagemSalva);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponse> alterar(@PathVariable Long id, @RequestBody @Valid ProdutoRequest produtoRequest){
        return ResponseEntity.ok(crudProdutoService.alterar(id, produtoRequest));
    }

    // TEST
    @PutMapping("/{idProduto}/image/{idImage}/")
    public ResponseEntity<ProdutoImagemResponse> alterarImagem(@PathVariable Long idProduto, @PathVariable Long idImage,
                                                               @RequestParam("file") MultipartFile file) throws IOException {

        crudProdutoService.buscarPorId(idProduto);
        return ResponseEntity.ok(imagemUploadService.uploadEAlteraImagem(idImage, file));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id){
        crudProdutoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}

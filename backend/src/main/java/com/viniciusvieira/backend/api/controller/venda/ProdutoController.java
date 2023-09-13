package com.viniciusvieira.backend.api.controller.venda;

import com.viniciusvieira.backend.api.representation.model.request.venda.ProdutoRequest;
import com.viniciusvieira.backend.api.representation.model.response.ProdutoImagemResponse;
import com.viniciusvieira.backend.api.representation.model.response.venda.ProdutoResponse;
import com.viniciusvieira.backend.domain.model.venda.Produto;
import com.viniciusvieira.backend.domain.model.venda.ProdutoImagem;
import com.viniciusvieira.backend.domain.service.CrudProdutoImagemService;
import com.viniciusvieira.backend.domain.service.venda.CrudProdutoService;
import com.viniciusvieira.backend.domain.service.ImageService;
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
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProdutoController {
    private final CrudProdutoService crudProdutoService;
    private final ImageService imageService;
    private final CrudProdutoImagemService crudProdutoImagemService;

    @GetMapping
    public ResponseEntity<List<Produto>> buscarTodos() {
        return ResponseEntity.ok(crudProdutoService.buscarTodos());
    }

    @GetMapping("/{idProduto}/imagens")
    public ResponseEntity<List<ProdutoImagem>> buscarImagensPorProduto(@PathVariable Long idProduto){
        return ResponseEntity
                .ok(crudProdutoImagemService.buscarPorProduto(idProduto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(crudProdutoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<ProdutoResponse> inserir(@RequestBody @Valid ProdutoRequest produtoRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(crudProdutoService.inserir(produtoRequest));
    }

    @PostMapping("/{idProduto}/image/")
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

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponse> alterar(@PathVariable Long id, @RequestBody @Valid ProdutoRequest produtoRequest) {
        return ResponseEntity.ok(crudProdutoService.alterar(id, produtoRequest));
    }

    // TEST
    @PutMapping("/{idProduto}/image/{idImage}/")
    public ResponseEntity<ProdutoImagemResponse> alterarImagem(
            @PathVariable Long idProduto,
            @PathVariable Long idImage,
            @RequestParam("file") MultipartFile file
    ) throws IOException {

        crudProdutoService.buscarPorId(idProduto);
        return ResponseEntity.ok(imageService.uploadEAlteraImagem(idImage, file));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        crudProdutoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}

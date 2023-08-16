package com.viniciusvieira.backend.api.controller.venda;

import com.viniciusvieira.backend.api.representation.model.request.venda.MarcaRequest;
import com.viniciusvieira.backend.api.representation.model.response.venda.MarcaResponse;
import com.viniciusvieira.backend.domain.model.venda.Marca;
import com.viniciusvieira.backend.domain.service.venda.CrudMarcaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/marcas")
@CrossOrigin(origins = "*", maxAge = 3600)
public class MarcaController {
    private final CrudMarcaService crudMarcaService;

    @GetMapping
    public ResponseEntity<List<Marca>> buscarTodos(){
        return ResponseEntity.ok(crudMarcaService.buscarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Marca> buscarPeloId(@PathVariable Long id){
        return ResponseEntity.ok(crudMarcaService.buscarPeloId(id));
    }

    @PostMapping
    public ResponseEntity<MarcaResponse> inserir(@RequestBody @Valid MarcaRequest marcaRequest){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(crudMarcaService.inserir(marcaRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MarcaResponse> alterar(@PathVariable Long id, @RequestBody @Valid MarcaRequest marcaRequest){
        return ResponseEntity.ok(crudMarcaService.alterar(id, marcaRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id){
        crudMarcaService.excluir(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}

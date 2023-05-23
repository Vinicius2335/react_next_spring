package com.viniciusvieira.backend.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

// NOTE excluir depois
@RestController
@RequestMapping("/api")
public class HelloController {
    // TODO criar controller para Marca e Estado

    @GetMapping("/hello")
    public ResponseEntity<String> hello(){
        return ResponseEntity.ok("Olá mundo, são " + LocalDateTime.now());
    }
}

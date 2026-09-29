package com.projeto_java.cafeteria.controller;

import com.projeto_java.cafeteria.infrastructure.entitys.Cafe;
import com.projeto_java.cafeteria.service.CafeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cafe")
@RequiredArgsConstructor
public class CafeController {

    private final CafeService cafeService;

    // Cadastra um novo café
    @PostMapping
    public ResponseEntity<Void> salvarCafe(
            @RequestBody Cafe cafe) {

        cafeService.salvarCafe(cafe);

        return ResponseEntity.ok().build();
    }

    // Busca um café pelo nome
    @GetMapping("/nome/{nome}")
    public ResponseEntity<Cafe> buscarCafePorNome(
            @PathVariable String nome) {

        Cafe cafe = cafeService.buscarCafePorNome(nome);

        return ResponseEntity.ok(cafe);
    }

    // Atualiza um café pelo ID
    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizarCafe(
            @PathVariable Integer id,
            @RequestBody Cafe cafe) {

        cafeService.atualizarCafePorId(id, cafe);

        return ResponseEntity.ok().build();
    }

    // Exclui um café pelo nome
    @DeleteMapping("/nome/{nome}")
    public ResponseEntity<Void> deletarCafePorNome(
            @PathVariable String nome) {

        cafeService.deletarCafePorNome(nome);

        return ResponseEntity.ok().build();
    }
}
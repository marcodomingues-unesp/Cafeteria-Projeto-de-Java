
package com.projeto_java.cafeteria.controller;

import com.projeto_java.cafeteria.infrastructure.entitys.Cafe;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cafes")
public class CafeController {

    private final CafeService cafeService;

    public CafeController(CafeService cafeService) {
        this.cafeService = cafeService;
    }

    // ==================== CADASTRAR CAFÉ ====================

    @PostMapping
    public ResponseEntity<?> cadastrarCafe(
            @RequestBody Cafe cafe) {

        try {
            Cafe salvo = cafeService.comprarCafe(cafe);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(salvo);

        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("erro", e.getMessage()));
        }
    }

    // ==================== LISTAR TODOS ====================

    @GetMapping
    public ResponseEntity<List<Cafe>> listarCafes() {

        List<Cafe> cafes = cafeService.listarCafes();

        return ResponseEntity.ok(cafes);
    }

    // ==================== BUSCAR POR ID ====================

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarCafe(
            @PathVariable Integer id) {

        try {
            Cafe cafe = cafeService.buscarCafePorId(id);

            return ResponseEntity.ok(cafe);

        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("erro", e.getMessage()));
        }
    }

    // ==================== ATUALIZAR ====================

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarCafe(
            @PathVariable Integer id,
            @RequestBody Cafe cafe) {

        try {
            Cafe atualizado = cafeService.alterarPedido(
                    id,
                    cafe
            );

            return ResponseEntity.ok(atualizado);

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(Map.of("erro", e.getMessage()));
        }
    }

    // ==================== EXCLUIR ====================

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluirCafe(
            @PathVariable Integer id) {

        try {
            cafeService.cancelarPedido(id);

            return ResponseEntity.noContent().build();

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("erro", e.getMessage()));
        }
    }
}

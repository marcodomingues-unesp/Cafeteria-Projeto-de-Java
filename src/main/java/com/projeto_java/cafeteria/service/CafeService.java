package com.projeto_java.cafeteria.service;

import com.projeto_java.cafeteria.infrastructure.entitys.Cafe;
import com.projeto_java.cafeteria.infrastructure.repository.CafeRepository;
import com.projeto_java.cafeteria.validator.CafeValidator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CafeService {

    private final CafeRepository repository;
    private final CafeValidator cafeValidator;

    public CafeService(
            CafeRepository repository,
            CafeValidator cafeValidator) {

        this.repository = repository;
        this.cafeValidator = cafeValidator;
    }

    public List<Cafe> listarPedidos() {
        return repository.selecionarCafe();
    }

    public void cadastrar(Cafe cafe) {

        validarCafe(cafe);

        repository.saveAndFlush(cafe);
    }

    public void alterar(Integer id, Cafe cafe) {

        validarCafe(cafe);

        Cafe cafeExistente = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Café não encontrado."
                        )
                );

        cafeExistente.setNomeProduto(
                cafe.getNomeProduto()
        );

        cafeExistente.setTamanhoProduto(
                cafe.getTamanhoProduto()
        );

        cafeExistente.setTipoTorra(
                cafe.getTipoTorra()
        );

        cafeExistente.setPreco(
                cafe.getPreco()
        );

        repository.saveAndFlush(cafeExistente);
    }

    public void cancelar(Integer id) {

        if (!repository.existsById(id)) {

            throw new RuntimeException(
                    "Café não encontrado."
            );
        }

        repository.deleteById(id);
    }

    public void excluirTodos() {

        repository.excluirTodosPedidos();
    }

    private void validarCafe(Cafe cafe) {

        if (!cafeValidator.validar(cafe)) {

            throw new IllegalArgumentException(
                    cafeValidator.getMensagemErro()
            );
        }
    }
}
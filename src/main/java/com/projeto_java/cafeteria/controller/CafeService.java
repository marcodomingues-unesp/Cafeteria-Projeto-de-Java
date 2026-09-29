
package com.projeto_java.cafeteria.controller;

import com.projeto_java.cafeteria.infrastructure.entitys.Cafe;
import com.projeto_java.cafeteria.infrastructure.repository.CafeRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CafeService {

    private static final Logger log =
            LoggerFactory.getLogger(CafeService.class);

    private final CafeRepository cafeRepository;

    public CafeService(CafeRepository cafeRepository) {
        this.cafeRepository = cafeRepository;
    }

    // ==================== LISTAR ====================

    @Transactional(readOnly = true)
    public List<Cafe> listarCafes() {
        return cafeRepository.selecionarCafe();
    }

    // ==================== BUSCAR POR ID ====================

    @Transactional(readOnly = true)
    public Cafe buscarCafePorId(Integer id) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    "Informe um ID válido."
            );
        }

        return cafeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Café não encontrado com o ID: " + id
                ));
    }

    // ==================== CADASTRAR ====================

    @Transactional
    public Cafe comprarCafe(Cafe cafe) {

        validarCafe(cafe);

        cafe.setId(null);

        cafe.setNomeProduto(cafe.getNomeProduto().trim());
        cafe.setTamanhoProduto(cafe.getTamanhoProduto().trim());
        cafe.setTipoTorra(cafe.getTipoTorra().trim());

        try {

            Cafe salvo = cafeRepository.saveAndFlush(cafe);

            log.info(
                    "Café cadastrado no PostgreSQL. ID: {}",
                    salvo.getId()
            );

            return salvo;

        } catch (Exception e) {

            log.error("Erro ao cadastrar café no PostgreSQL.", e);

            throw new RuntimeException(
                    "Não foi possível salvar o café no banco de dados.",
                    e
            );
        }
    }

    @Transactional
    public Cafe salvarCafe(Cafe cafe) {
        return comprarCafe(cafe);
    }

    // ==================== ALTERAR ====================

    @Transactional
    public Cafe alterarPedido(Integer id, Cafe dadosAtualizados) {

        validarCafe(dadosAtualizados);

        Cafe cafeExistente = buscarCafePorId(id);

        cafeExistente.setNomeProduto(
                dadosAtualizados.getNomeProduto().trim()
        );

        cafeExistente.setTamanhoProduto(
                dadosAtualizados.getTamanhoProduto().trim()
        );

        cafeExistente.setTipoTorra(
                dadosAtualizados.getTipoTorra().trim()
        );

        cafeExistente.setPreco(dadosAtualizados.getPreco());

        Cafe atualizado = cafeRepository.saveAndFlush(cafeExistente);

        log.info("Café atualizado. ID: {}", atualizado.getId());

        return atualizado;
    }

    @Transactional
    public Cafe atualizarCafePorId(Integer id, Cafe cafe) {
        return alterarPedido(id, cafe);
    }

    // ==================== BUSCAR POR NOME ====================

    @Transactional(readOnly = true)
    public Cafe buscarCafePorNome(String nome) {

        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException(
                    "O nome do café deve ser informado."
            );
        }

        return cafeRepository.findAll()
                .stream()
                .filter(cafe ->
                        cafe.getNomeProduto() != null
                                && cafe.getNomeProduto()
                                .equalsIgnoreCase(nome.trim())
                )
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Café não encontrado: " + nome
                ));
    }

    // ==================== CANCELAR PEDIDO ====================

    @Transactional
    public void cancelarPedido(Integer id) {

        Cafe cafe = buscarCafePorId(id);

        cafeRepository.delete(cafe);
        cafeRepository.flush();

        log.info("Café excluído. ID: {}", id);
    }

    // ==================== DELETAR POR NOME ====================

    @Transactional
    public void deletarCafePorNome(String nome) {

        Cafe cafe = buscarCafePorNome(nome);

        cafeRepository.delete(cafe);
        cafeRepository.flush();

        log.info("Café excluído pelo nome: {}", nome);
    }

    // ==================== EXCLUIR TODOS ====================

    @Transactional
    public void excluirTodosPedidos() {

        cafeRepository.excluirTodosPedidos();

        log.info(
                "Todos os cafés foram excluídos. Sequência de IDs reiniciada."
        );
    }

    // ==================== VALIDAÇÃO ====================

    private void validarCafe(Cafe cafe) {

        if (cafe == null) {
            throw new IllegalArgumentException(
                    "Os dados do café não foram informados."
            );
        }

        if (cafe.getNomeProduto() == null
                || cafe.getNomeProduto().isBlank()) {
            throw new IllegalArgumentException(
                    "O nome do produto é obrigatório."
            );
        }

        if (cafe.getTamanhoProduto() == null
                || cafe.getTamanhoProduto().isBlank()) {
            throw new IllegalArgumentException(
                    "O tamanho do produto é obrigatório."
            );
        }

        if (cafe.getTipoTorra() == null
                || cafe.getTipoTorra().isBlank()) {
            throw new IllegalArgumentException(
                    "O tipo de torra é obrigatório."
            );
        }

        if (cafe.getPreco() == null
                || !Double.isFinite(cafe.getPreco())
                || cafe.getPreco() <= 0) {
            throw new IllegalArgumentException(
                    "O preço deve ser um número válido maior que zero."
            );
        }
    }
}

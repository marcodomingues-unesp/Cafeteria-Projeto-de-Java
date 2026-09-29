package com.projeto_java.cafeteria.validator;

import lombok.Getter;

@Getter
public class PrecoValidator {

    // Retorna o valor que será validado
    private final Double valor;
    // Retorna a mensagem de erro
    private String mensagemErro;

    public PrecoValidator(Double valor) {
        this.valor = valor;
    }

    // Valida o preço do produto
    public boolean validar(Double preco) {

        // Verifica se o preço é maior que zero
        if (preco == null || preco <= 0) {
            mensagemErro = "O preço deve ser maior que zero.";
            return false;
        }

        // Indica que a validação foi aprovada
        mensagemErro = null;
        return true;
    }

}
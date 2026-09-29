package com.projeto_java.cafeteria.validator;

import lombok.Getter;

import java.util.regex.Pattern;

@Getter
public class NomeValidator {

    // Define o formato permitido para o nome
    private static final Pattern NOME_PATTERN =
            Pattern.compile("^[a-zA-ZÀ-ÿ ]+$");

    // Retorna o valor que será validado
    private final String valor;
    // Retorna a mensagem de erro
    private String mensagemErro;

    public NomeValidator(String valor) {
        this.valor = valor;
    }

    // Valida o nome do produto
    public boolean validar(String nome) {

        // Verifica se o nome foi preenchido
        if (nome == null || nome.trim().isEmpty()) {
            mensagemErro = "Informe o nome do produto.";
            return false;
        }

        // Verifica se o nome possui apenas caracteres válidos
        if (!NOME_PATTERN.matcher(nome.trim()).matches()) {
            mensagemErro = "Digite um nome de produto válido.";
            return false;
        }

        // Indica que a validação foi aprovada
        mensagemErro = null;
        return true;
    }

}
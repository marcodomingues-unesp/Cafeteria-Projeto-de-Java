package com.projeto_java.cafeteria.validator;

import lombok.Getter;

public class CampoObrigatorioValidator {

    private final String nomeCampo;
    // Retorna o valor que será validado
    @Getter
    private final String valor;

    public CampoObrigatorioValidator(String nomeCampo, String valor) {
        this.nomeCampo = nomeCampo;
        this.valor = valor;
    }

    // Verifica se o campo foi preenchido
    public boolean validar(String valorAtual) {
        return valorAtual == null
                || valorAtual.trim().isEmpty();
    }

    // Retorna a mensagem do erro
    public String getMensagemErro() {
        return "O campo " + getCampo()
                + " deve ser preenchido.";
    }

    // Retorna o nome do campo
    public String getCampo() {
        return nomeCampo;
    }
}
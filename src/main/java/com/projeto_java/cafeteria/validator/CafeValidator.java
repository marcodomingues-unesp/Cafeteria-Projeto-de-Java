package com.projeto_java.cafeteria.validator;

import com.projeto_java.cafeteria.infrastructure.entitys.Cafe;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class CafeValidator {

    private String mensagemErro;

    public boolean validar(Cafe dadosCafe) {

        if (dadosCafe == null) {
            mensagemErro = "Informe os dados do café.";
            return false;
        }

        // Validação do nome
        CampoObrigatorioValidator nomeObrigatorio =
                new CampoObrigatorioValidator(
                        "Nome do produto",
                        dadosCafe.getNomeProduto()
                );

        if (nomeObrigatorio.validar(nomeObrigatorio.getValor())) {
            mensagemErro = nomeObrigatorio.getMensagemErro();
            return false;
        }

        NomeValidator nomeValidator =
                new NomeValidator(
                        dadosCafe.getNomeProduto()
                );

        if (!nomeValidator.validar(nomeValidator.getValor())) {
            mensagemErro = nomeValidator.getMensagemErro();
            return false;
        }

        // Validação do tamanho
        CampoObrigatorioValidator tamanhoObrigatorio =
                new CampoObrigatorioValidator(
                        "Tamanho do produto",
                        dadosCafe.getTamanhoProduto()
                );

        if (tamanhoObrigatorio.validar(
                tamanhoObrigatorio.getValor())) {

            mensagemErro = tamanhoObrigatorio.getMensagemErro();
            return false;
        }

        // Validação do tipo de torra
        CampoObrigatorioValidator torraObrigatoria =
                new CampoObrigatorioValidator(
                        "Tipo de torra",
                        dadosCafe.getTipoTorra()
                );

        if (torraObrigatoria.validar(
                torraObrigatoria.getValor())) {

            mensagemErro = torraObrigatoria.getMensagemErro();
            return false;
        }

        // Validação do preço
        PrecoValidator precoValidator =
                new PrecoValidator(
                        dadosCafe.getPreco()
                );

        if (!precoValidator.validar(
                precoValidator.getValor())) {

            mensagemErro = precoValidator.getMensagemErro();
            return false;
        }

        mensagemErro = null;
        return true;
    }

}
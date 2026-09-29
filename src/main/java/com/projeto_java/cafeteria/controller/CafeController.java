package com.projeto_java.cafeteria.controller;

import com.projeto_java.cafeteria.infrastructure.entitys.Cafe;
import com.projeto_java.cafeteria.service.CafeService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.stereotype.Component;

@Component
public class CafeController {

    private final CafeService cafeService;

    @FXML
    private TextField txtNome;

    @FXML
    private TextField txtPreco;

    @FXML
    private ComboBox<String> comboTamanho;

    @FXML
    private ComboBox<String> comboTorra;

    @FXML
    private Label lblMensagem;

    @FXML
    private TableView<Cafe> tablePedidos;

    @FXML
    private TableColumn<Cafe, Integer> colId;

    @FXML
    private TableColumn<Cafe, String> colNome;

    @FXML
    private TableColumn<Cafe, String> colTamanho;

    @FXML
    private TableColumn<Cafe, String> colTorra;

    @FXML
    private TableColumn<Cafe, Double> colPreco;

    public CafeController(CafeService cafeService) {
        this.cafeService = cafeService;
    }

    @FXML
    public void initialize() {

        comboTamanho.setItems(
                FXCollections.observableArrayList(
                        "Pequeno",
                        "Médio",
                        "Grande"
                )
        );

        comboTorra.setItems(
                FXCollections.observableArrayList(
                        "Clara",
                        "Média",
                        "Escura"
                )
        );

        configurarTabela();
        carregarPedidos();

        tablePedidos.getSelectionModel()
                .selectedItemProperty()
                .addListener((_, _, selecionado) -> {

                    if (selecionado != null) {
                        preencherFormulario(selecionado);
                    }
                });
    }

    private void configurarTabela() {

        colId.setCellValueFactory(
                new PropertyValueFactory<>("id")
        );

        colNome.setCellValueFactory(
                new PropertyValueFactory<>("nomeProduto")
        );

        colTamanho.setCellValueFactory(
                new PropertyValueFactory<>("tamanhoProduto")
        );

        colTorra.setCellValueFactory(
                new PropertyValueFactory<>("tipoTorra")
        );

        colPreco.setCellValueFactory(
                new PropertyValueFactory<>("preco")
        );
    }

    private void carregarPedidos() {

        ObservableList<Cafe> pedidos =
                FXCollections.observableArrayList(
                        cafeService.listarPedidos()
                );

        tablePedidos.setItems(pedidos);
    }

    @FXML
    public void comprarCafe() {

        try {

            Cafe cafe = criarCafeFormulario();

            cafeService.cadastrar(cafe);

            mostrarMensagem(
                    "Pedido adicionado com sucesso."
            );

            carregarPedidos();
            limparCampos();

        } catch (Exception erro) {

            mostrarMensagem(
                    obterMensagemErro(erro)
            );
        }
    }

    @FXML
    public void alterarPedido() {

        Cafe cafeSelecionado =
                tablePedidos.getSelectionModel()
                        .getSelectedItem();

        if (cafeSelecionado == null) {

            mostrarMensagem(
                    "Selecione um pedido para alterar."
            );

            return;
        }

        try {

            Cafe cafe = criarCafeFormulario();

            cafeService.alterar(
                    cafeSelecionado.getId(),
                    cafe
            );

            mostrarMensagem(
                    "Pedido alterado com sucesso."
            );

            carregarPedidos();
            limparCampos();

        } catch (Exception erro) {

            mostrarMensagem(
                    obterMensagemErro(erro)
            );
        }
    }

    @FXML
    public void cancelarPedido() {

        Cafe cafeSelecionado =
                tablePedidos.getSelectionModel()
                        .getSelectedItem();

        if (cafeSelecionado == null) {

            mostrarMensagem(
                    "Selecione um pedido para cancelar."
            );

            return;
        }

        Alert confirmacao = new Alert(
                Alert.AlertType.CONFIRMATION
        );

        confirmacao.setTitle("Cancelar pedido");

        confirmacao.setHeaderText(
                "Deseja cancelar este pedido?"
        );

        confirmacao.setContentText(
                "Pedido: " + cafeSelecionado.getNomeProduto()
        );

        if (confirmacao.showAndWait()
                .orElse(ButtonType.CANCEL)
                != ButtonType.OK) {

            return;
        }

        try {

            cafeService.cancelar(
                    cafeSelecionado.getId()
            );

            mostrarMensagem(
                    "Pedido cancelado com sucesso."
            );

            carregarPedidos();
            limparCampos();

        } catch (Exception erro) {

            mostrarMensagem(
                    obterMensagemErro(erro)
            );
        }
    }

    @FXML
    public void limparFormulario() {

        limparCampos();

        mostrarMensagem(
                "Formulário limpo."
        );
    }

    @FXML
    public void excluirTudo() {

        Alert confirmacao = new Alert(
                Alert.AlertType.CONFIRMATION
        );

        confirmacao.setTitle("Excluir pedidos");

        confirmacao.setHeaderText(
                "Excluir todos os pedidos?"
        );

        confirmacao.setContentText(
                "Essa ação não poderá ser desfeita."
        );

        if (confirmacao.showAndWait()
                .orElse(ButtonType.CANCEL)
                != ButtonType.OK) {

            return;
        }

        try {

            cafeService.excluirTodos();

            mostrarMensagem(
                    "Todos os pedidos foram excluídos."
            );

            carregarPedidos();
            limparCampos();

        } catch (Exception erro) {

            mostrarMensagem(
                    obterMensagemErro(erro)
            );
        }
    }

    private Cafe criarCafeFormulario() {

        String precoTexto =
                txtPreco.getText()
                        .trim()
                        .replace(",", ".");

        return Cafe.builder()
                .nomeProduto(
                        txtNome.getText().trim()
                )
                .tamanhoProduto(
                        comboTamanho.getValue()
                )
                .tipoTorra(
                        comboTorra.getValue()
                )
                .preco(
                        Double.parseDouble(precoTexto)
                )
                .build();
    }

    private void preencherFormulario(Cafe cafe) {

        txtNome.setText(
                cafe.getNomeProduto()
        );

        comboTamanho.setValue(
                cafe.getTamanhoProduto()
        );

        comboTorra.setValue(
                cafe.getTipoTorra()
        );

        if (cafe.getPreco() != null) {

            txtPreco.setText(
                    String.valueOf(
                            cafe.getPreco()
                    )
            );
        }
    }

    private void limparCampos() {

        txtNome.clear();

        txtPreco.clear();

        comboTamanho.getSelectionModel()
                .clearSelection();

        comboTorra.getSelectionModel()
                .clearSelection();

        tablePedidos.getSelectionModel()
                .clearSelection();
    }

    private void mostrarMensagem(String mensagem) {

        if (mensagem == null || mensagem.isBlank()) {

            lblMensagem.setText("");

            return;
        }

        lblMensagem.setText(mensagem);
    }

    private String obterMensagemErro(Exception erro) {

        if (erro.getMessage() == null
                || erro.getMessage().isBlank()) {

            return "Ocorreu um erro ao realizar a operação.";
        }

        return erro.getMessage();
    }
}
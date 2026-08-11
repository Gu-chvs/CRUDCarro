package com.template.controller;

import com.template.model.CarroDAO;
import com.template.model.CarroDTO;
import com.template.util.DialogUtil;
import com.template.validator.CarroValidator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.ArrayList;

public class MainController {

    @FXML private Label lblStatus;

    @FXML private Button btnExcluir;
    @FXML private Button btnAdicionar;
    @FXML private Button btnEditar;
    @FXML private Button btnLimpar;

    @FXML private TableView<CarroDTO> tblCarro;
    @FXML private TableColumn<CarroDTO, Integer> colId;
    @FXML private TableColumn<CarroDTO, String> colMarca;
    @FXML private TableColumn<CarroDTO, String> colModelo;
    @FXML private TableColumn<CarroDTO, Integer> colAnoFabricacao;
    @FXML private TableColumn<CarroDTO, String> colPlaca;

    @FXML private TextField txtId;
    @FXML private TextField txtMarca;
    @FXML private TextField txtModelo;
    @FXML private TextField txtAnoFabricacao;
    @FXML private TextField txtPlaca;

    @FXML private TextField txtPesquisa;

    private final ObservableList<CarroDTO> listaCarrosMaster = FXCollections.observableArrayList();
    private final CarroValidator validator = new CarroValidator();

    // Executa a adição de um novo veículo após validar os campos
    @FXML
    private void btnAdicionarAction(ActionEvent event) {
        if (!validator.validarCampos(txtMarca.getText(), txtModelo.getText(), txtAnoFabricacao.getText())) {
            DialogUtil.showWarning("Por favor, preencha todos os campos obrigatórios corretamente!");
            return;
        }

        try {
            CarroDTO objcarrodto = new CarroDTO();
            objcarrodto.setMarca(txtMarca.getText().trim());
            objcarrodto.setModelo(txtModelo.getText().trim());
            objcarrodto.setAnoFabricacao(Integer.parseInt(txtAnoFabricacao.getText().trim()));
            objcarrodto.setPlaca(txtPlaca.getText().trim());

            CarroDAO objcarrodao = new CarroDAO();
            boolean sucesso = objcarrodao.inserirCarro(objcarrodto);

            if (sucesso) {
                mostrarMensagem("Sucesso: Carro adicionado ao sistema!", "#2ECC71");
                btnLimparAction(null);
                carregarCarros();
            } else {
                DialogUtil.showError("Não foi possível adicionar o carro no banco.");
            }
        } catch (Exception e) {
            DialogUtil.showError("Erro inesperado ao adicionar: Verifique os dados inseridos.");
        }
    }

    // Executa a atualização do veículo selecionado após validar os campos
    @FXML
    private void btnEditarAction(ActionEvent event) {
        if (!validator.validarCampos(txtMarca.getText(), txtModelo.getText(), txtAnoFabricacao.getText())) {
            DialogUtil.showWarning("Por favor, preencha todos os campos obrigatórios corretamente!");
            return;
        }

        try {
            CarroDTO objcarrodto = new CarroDTO();
            objcarrodto.setId(Integer.parseInt(txtId.getText()));
            objcarrodto.setMarca(txtMarca.getText().trim());
            objcarrodto.setModelo(txtModelo.getText().trim());
            objcarrodto.setAnoFabricacao(Integer.parseInt(txtAnoFabricacao.getText().trim()));
            objcarrodto.setPlaca(txtPlaca.getText().trim());

            CarroDAO objcarrodao = new CarroDAO();
            boolean sucesso = objcarrodao.atualizarCarro(objcarrodto);

            if (sucesso) {
                mostrarMensagem("Sucesso: Carro atualizado corretamente!", "#3498DB");
                btnLimparAction(null);
                carregarCarros();
            } else {
                DialogUtil.showError("Erro ao atualizar o carro no banco de dados.");
            }
        } catch (Exception e) {
            DialogUtil.showError("Selecione um carro e verifique os dados.");
        }
    }

    // Executa a exclusão do veículo selecionado na tabela
    @FXML
    private void btnExcluirAction(ActionEvent event) {
        try {
            int id = Integer.parseInt(txtId.getText());
            CarroDAO objcarrodao = new CarroDAO();
            boolean sucesso = objcarrodao.excluirCarro(id);

            if (sucesso) {
                mostrarMensagem("Sucesso: Carro excluído do sistema!", "#E74C3C");
                btnLimparAction(null);
                carregarCarros();
            } else {
                DialogUtil.showError("Erro ao excluir o carro.");
            }
        } catch (Exception e) {
            DialogUtil.showError("Selecione um carro para excluir.");
        }
    }

    // Reseta todos os campos de texto e a seleção da tabela
    @FXML
    private void btnLimparAction(ActionEvent event) {
        txtId.clear();
        txtMarca.clear();
        txtModelo.clear();
        txtPlaca.clear();
        txtAnoFabricacao.clear();
        txtPesquisa.clear();
        tblCarro.getSelectionModel().clearSelection();
        txtMarca.requestFocus();
        mostrarMensagem("Campos limpos. Pronto para novo cadastro.", "#a1a1a1");
    }

    // Consulta os veículos no banco de dados e atualiza a lista principal
    @FXML
    private void carregarCarros() {
        CarroDAO objCarroDAO = new CarroDAO();
        ArrayList<CarroDTO> lista = objCarroDAO.selecionarCarros();
        listaCarrosMaster.setAll(lista);
    }

    // Preenche os campos do formulário com os dados da linha selecionada na tabela
    @FXML
    private void carregarCampos() {
        CarroDTO carroDTO = tblCarro.getSelectionModel().getSelectedItem();
        if (carroDTO != null) {
            txtId.setText(String.valueOf(carroDTO.getId()));
            txtMarca.setText(carroDTO.getMarca());
            txtModelo.setText(carroDTO.getModelo());
            txtAnoFabricacao.setText(String.valueOf(carroDTO.getAnoFabricacao()));
            txtPlaca.setText(carroDTO.getPlaca());
            mostrarMensagem("Veículo selecionado. Pronto para alteração ou exclusão.", "#3498DB");
        }
    }

    // Exibe texto estilizado no rótulo de status da tela
    private void mostrarMensagem(String mensagem, String cor) {
        lblStatus.setText(mensagem);
        lblStatus.setStyle("-fx-text-fill: " + cor + ";");
    }

    // Configura o comportamento inicial dos componentes e da busca da interface
    @FXML
    private void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        colModelo.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        colAnoFabricacao.setCellValueFactory(new PropertyValueFactory<>("anoFabricacao"));
        colPlaca.setCellValueFactory(new PropertyValueFactory<>("placa"));

        txtAnoFabricacao.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                txtAnoFabricacao.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        tblCarro.getSelectionModel().selectedItemProperty().addListener((obs, selecaoAntiga, novaSelecao) -> {
            if (novaSelecao != null) {
                btnEditar.setDisable(false);
                btnExcluir.setDisable(false);
                btnAdicionar.setDisable(true);
            } else {
                btnEditar.setDisable(true);
                btnExcluir.setDisable(true);
                btnAdicionar.setDisable(false);
            }
        });

        FilteredList<CarroDTO> dadosFiltrados = new FilteredList<>(listaCarrosMaster, p -> true);

        txtPesquisa.textProperty().addListener((observable, oldValue, newValue) -> {
            dadosFiltrados.setPredicate(carro -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String termoBusca = newValue.toLowerCase().trim();

                if (carro.getMarca().toLowerCase().contains(termoBusca)) {
                    return true;
                } else if (carro.getModelo().toLowerCase().contains(termoBusca)) {
                    return true;
                } else if (carro.getPlaca() != null && carro.getPlaca().toLowerCase().contains(termoBusca)) {
                    return true;
                } else if (String.valueOf(carro.getId()).contains(termoBusca)) {
                    return true;
                }

                return false;
            });
        });

        SortedList<CarroDTO> dadosOrdenados = new SortedList<>(dadosFiltrados);
        dadosOrdenados.comparatorProperty().bind(tblCarro.comparatorProperty());

        tblCarro.setItems(dadosOrdenados);

        carregarCarros();
    }
}
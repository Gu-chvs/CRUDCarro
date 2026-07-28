package com.template.controller;

import com.template.model.CarroDAO;
import com.template.model.CarroDTO;
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

    // Novo componente para a busca em tempo real
    @FXML private TextField txtPesquisa;

    // Lista mestre que vai guardar os carros na memória para o filtro atuar por cima
    private final ObservableList<CarroDTO> listaCarrosMaster = FXCollections.observableArrayList();

    @FXML
    private void btnAdicionarAction(ActionEvent event) {
        try {
            if(txtMarca.getText().isEmpty() || txtModelo.getText().isEmpty() || txtAnoFabricacao.getText().isEmpty()) {
                mostrarMensagem("Erro: Preencha todos os campos obrigatórios!", "red");
                return;
            }

            CarroDTO objcarrodto = new CarroDTO();
            objcarrodto.setMarca(txtMarca.getText());
            objcarrodto.setModelo(txtModelo.getText());
            objcarrodto.setAnoFabricacao(Integer.parseInt(txtAnoFabricacao.getText()));
            objcarrodto.setPlaca(txtPlaca.getText());

            CarroDAO objcarrodao = new CarroDAO();
            boolean sucesso = objcarrodao.inserirCarro(objcarrodto);

            if (sucesso) {
                mostrarMensagem("Sucesso: Carro adicionado ao sistema!", "#2ECC71");
                btnLimparAction(null);
                carregarCarros();
            } else {
                mostrarMensagem("Erro: Não foi possível adicionar o carro no banco.", "red");
            }
        } catch (Exception e) {
            mostrarMensagem("Erro inesperado ao adicionar: Verifique os dados inseridos.", "red");
        }
    }

    @FXML
    private void btnEditarAction(ActionEvent event) {
        try {
            CarroDTO objcarrodto = new CarroDTO();
            objcarrodto.setId(Integer.parseInt(txtId.getText()));
            objcarrodto.setMarca(txtMarca.getText());
            objcarrodto.setModelo(txtModelo.getText());
            objcarrodto.setAnoFabricacao(Integer.parseInt(txtAnoFabricacao.getText()));
            objcarrodto.setPlaca(txtPlaca.getText());

            CarroDAO objcarrodao = new CarroDAO();
            boolean sucesso = objcarrodao.atualizarCarro(objcarrodto);

            if(sucesso) {
                mostrarMensagem("Sucesso: Carro atualizado corretamente!", "#3498DB");
                btnLimparAction(null);
                carregarCarros();
            } else {
                mostrarMensagem("Erro ao atualizar o carro no banco de dados.", "red");
            }
        } catch (Exception e) {
            mostrarMensagem("Erro: Selecione um carro e verifique os dados.", "red");
        }
    }

    @FXML
    private void btnExcluirAction(ActionEvent event) {
        try {
            int id = Integer.parseInt(txtId.getText());
            CarroDAO objcarrodao = new CarroDAO();
            boolean sucesso = objcarrodao.excluirCarro(id);

            if(sucesso) {
                mostrarMensagem("Sucesso: Carro excluído do sistema!", "#E74C3C");
                btnLimparAction(null);
                carregarCarros();
            } else {
                mostrarMensagem("Erro ao excluir o carro.", "red");
            }
        } catch (Exception e) {
            mostrarMensagem("Erro: Selecione um carro para excluir.", "red");
        }
    }

    @FXML
    private void btnLimparAction(ActionEvent event){
        txtId.clear();
        txtMarca.clear();
        txtModelo.clear();
        txtPlaca.clear();
        txtAnoFabricacao.clear();
        txtPesquisa.clear(); // Limpa também o campo de pesquisa ao resetar
        tblCarro.getSelectionModel().clearSelection();
        txtMarca.requestFocus();
        mostrarMensagem("Campos limpos. Pronto para novo cadastro.", "#a1a1a1");
    }

    @FXML
    private void carregarCarros() {
        CarroDAO objCarroDAO = new CarroDAO();
        ArrayList<CarroDTO> lista = objCarroDAO.selecionarCarros();

        // Atualiza a nossa lista mestre na memória
        listaCarrosMaster.setAll(lista);
    }

    @FXML
    private void carregarCampos(){
        CarroDTO carroDTO = tblCarro.getSelectionModel().getSelectedItem();
        if(carroDTO != null){
            txtId.setText(String.valueOf(carroDTO.getId()));
            txtMarca.setText(carroDTO.getMarca());
            txtModelo.setText(carroDTO.getModelo());
            txtAnoFabricacao.setText(String.valueOf(carroDTO.getAnoFabricacao()));
            txtPlaca.setText(carroDTO.getPlaca());
            mostrarMensagem("Veículo selecionado. Pronto para alteração ou exclusão.", "#3498DB");
        }
    }

    private void mostrarMensagem(String mensagem, String cor) {
        lblStatus.setText(mensagem);
        lblStatus.setStyle("-fx-text-fill: " + cor + ";");
    }

    @FXML
    private void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        colModelo.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        colAnoFabricacao.setCellValueFactory(new PropertyValueFactory<>("anoFabricacao"));
        colPlaca.setCellValueFactory(new PropertyValueFactory<>("placa"));

        // Trava para o campo Ano de Fabricação aceitar apenas números
        txtAnoFabricacao.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                txtAnoFabricacao.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        // Habilita/Desabilita botões baseado na seleção da tabela
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

        // CONFIGURAÇÃO DO FILTRO INTELIGENTE EM TEMPO REAL:
        // 1. Envolvemos a lista mestre em uma FilteredList (Começa mostrando tudo: p -> true)
        FilteredList<CarroDTO> dadosFiltrados = new FilteredList<>(listaCarrosMaster, p -> true);

        // 2. Adicionamos um Listener no campo de texto de pesquisa
        txtPesquisa.textProperty().addListener((observable, oldValue, newValue) -> {
            dadosFiltrados.setPredicate(carro -> {
                // Se o campo estiver vazio, mostra todos os registros
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String termoBusca = newValue.toLowerCase().trim();

                // Compara as colunas com o termo digitado
                if (carro.getMarca().toLowerCase().contains(termoBusca)) {
                    return true;
                } else if (carro.getModelo().toLowerCase().contains(termoBusca)) {
                    return true;
                } else if (carro.getPlaca() != null && carro.getPlaca().toLowerCase().contains(termoBusca)) {
                    return true;
                } else if (String.valueOf(carro.getId()).contains(termoBusca)) {
                    return true;
                }

                return false; // Não encontrou nenhum match na linha
            });
        });

        // 3. Envolvemos a lista filtrada em uma SortedList para que a ordenação das colunas continue funcionando
        SortedList<CarroDTO> dadosOrdenados = new SortedList<>(dadosFiltrados);
        dadosOrdenados.comparatorProperty().bind(tblCarro.comparatorProperty());

        // 4. Injeta os dados tratados na tabela
        tblCarro.setItems(dadosOrdenados);

        // Busca inicial do banco
        carregarCarros();
    }
}
package com.template.controller;

import com.template.model.dto.CarroDTO;
import com.template.service.ICarroService;
import com.template.util.DialogUtil;
import com.template.validator.ICarroValidador;
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

    // Injeção de dependência via interface do Service (backend de regras e dados)
    private final ICarroService carroService;

    // Construtor principal utilizado pela Fábrica de Controladores
    public MainController(ICarroService carroService) {
        this.carroService = carroService;
    }

    // Construtor sobrecarregado para retrocompatibilidade
    public MainController(ICarroService carroService, ICarroValidador validador) {
        this(carroService);
    }

    // Ação do FXML para adicionar veículo - delega a criação e validações ao backend (Service)
    @FXML
    private void btnAdicionarAction(ActionEvent event) {
        try {
            carroService.cadastrar(
                    txtMarca.getText(),
                    txtModelo.getText(),
                    txtAnoFabricacao.getText(),
                    txtPlaca.getText()
            );

            DialogUtil.showInfo("Carro cadastrado com sucesso!");
            mostrarMensagem("Sucesso: Carro adicionado ao sistema!", "#2ECC71");
            btnLimparAction(null);
            carregarCarros();
        } catch (IllegalArgumentException e) {
            DialogUtil.showWarning(e.getMessage());
            mostrarMensagem("Aviso: " + e.getMessage(), "#E67E22");
        } catch (Exception e) {
            DialogUtil.showError("Erro inesperado ao cadastrar: " + e.getMessage());
        }
    }

    // Ação do FXML para editar veículo - confirmação visual e delegação ao backend (Service)
    @FXML
    private void btnEditarAction(ActionEvent event) {
        if (!DialogUtil.showConfirmation("Deseja realmente atualizar as informações deste veículo?")) {
            return;
        }

        try {
            carroService.atualizar(
                    txtId.getText(),
                    txtMarca.getText(),
                    txtModelo.getText(),
                    txtAnoFabricacao.getText(),
                    txtPlaca.getText()
            );

            DialogUtil.showInfo("Carro atualizado com sucesso!");
            mostrarMensagem("Sucesso: Carro atualizado corretamente!", "#3498DB");
            btnLimparAction(null);
            carregarCarros();
        } catch (IllegalArgumentException e) {
            DialogUtil.showWarning(e.getMessage());
            mostrarMensagem("Aviso: " + e.getMessage(), "#E67E22");
        } catch (Exception e) {
            DialogUtil.showError("Erro inesperado ao atualizar: " + e.getMessage());
        }
    }

    // Ação do FXML para excluir veículo - confirmação visual e delegação ao backend (Service)
    @FXML
    private void btnExcluirAction(ActionEvent event) {
        if (!DialogUtil.showConfirmation("Tem certeza de que deseja excluir o veículo selecionado?")) {
            return;
        }

        try {
            carroService.excluir(txtId.getText());

            DialogUtil.showInfo("Carro excluído com sucesso!");
            mostrarMensagem("Sucesso: Carro excluído do sistema!", "#E74C3C");
            btnLimparAction(null);
            carregarCarros();
        } catch (IllegalArgumentException e) {
            DialogUtil.showWarning(e.getMessage());
            mostrarMensagem("Aviso: " + e.getMessage(), "#E67E22");
        } catch (Exception e) {
            DialogUtil.showError("Erro ao excluir o carro: " + e.getMessage());
        }
    }

    // Reseta todos os campos visuais e a seleção da tabela
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

    // Consulta os veículos através da camada de serviço e atualiza a lista observável
    @FXML
    private void carregarCarros() {
        listaCarrosMaster.setAll(carroService.listarCarros());
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

    // Configuração inicial dos componentes da interface gráfica
    @FXML
    private void initialize() {
        configurarColunasTabela();
        configurarSelecaoDeLinha();
        configurarPesquisa();
        carregarCarros();
    }

    // Associa cada coluna da tabela à propriedade correspondente do DTO
    private void configurarColunasTabela() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        colModelo.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        colAnoFabricacao.setCellValueFactory(new PropertyValueFactory<>("anoFabricacao"));
        colPlaca.setCellValueFactory(new PropertyValueFactory<>("placa"));
    }

    // Controla o estado de ativação dos botões da interface conforme a seleção na tabela
    private void configurarSelecaoDeLinha() {
        tblCarro.getSelectionModel().selectedItemProperty().addListener((obs, selecaoAntiga, novaSelecao) -> {
            boolean temSelecao = novaSelecao != null;
            btnEditar.setDisable(!temSelecao);
            btnExcluir.setDisable(!temSelecao);
            btnAdicionar.setDisable(temSelecao);
        });
    }

    // Vincula o campo de pesquisa do FXML ao filtro e ordenação da TableView
    private void configurarPesquisa() {
        FilteredList<CarroDTO> dadosFiltrados = new FilteredList<>(listaCarrosMaster, p -> true);

        txtPesquisa.textProperty().addListener((observable, oldValue, newValue) ->
                dadosFiltrados.setPredicate(carro -> carroService.correspondeATermo(carro, newValue)));

        SortedList<CarroDTO> dadosOrdenados = new SortedList<>(dadosFiltrados);
        dadosOrdenados.comparatorProperty().bind(tblCarro.comparatorProperty());

        tblCarro.setItems(dadosOrdenados);
    }
}
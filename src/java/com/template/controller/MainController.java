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

    // Injeção de dependência via interfaces (Requisitos 5 e 6: ISeuAssuntoValidador)
    private final ICarroService carroService;
    private final ICarroValidador validador;

    // Construtor utilizado pela Fábrica de Controladores no Main (Requisito 6 e 7)
    public MainController(ICarroService carroService, ICarroValidador validador) {
        this.carroService = carroService;
        this.validador = validador;
    }

    // Executa a adição (cadastro) de um novo veículo após validar os campos (Requisito 5)
    @FXML
    private void btnAdicionarAction(ActionEvent event) {
        // Validação obrigatória no cadastro usando ICarroValidador
        if (!validador.validarCampos(txtMarca.getText(), txtModelo.getText(), txtAnoFabricacao.getText())) {
            return;
        }

        try {
            CarroDTO carro = carroService.criarComDados(txtMarca.getText(), txtModelo.getText(), txtAnoFabricacao.getText(), txtPlaca.getText());
            boolean sucesso = carroService.cadastrarCarro(carro);

            if (sucesso) {
                DialogUtil.showInfo("Carro cadastrado com sucesso!");
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

    // Executa a atualização do veículo selecionado após validação e confirmação (Requisito 5)
    @FXML
    private void btnEditarAction(ActionEvent event) {
        // Validação obrigatória na atualização usando ICarroValidador
        if (!validador.validarCampos(txtMarca.getText(), txtModelo.getText(), txtAnoFabricacao.getText())) {
            return;
        }

        if (!DialogUtil.showConfirmation("Deseja realmente atualizar as informações deste veículo?")) {
            return;
        }

        try {
            CarroDTO carro = carroService.criarComDados(txtMarca.getText(), txtModelo.getText(), txtAnoFabricacao.getText(), txtPlaca.getText());
            carro.setId(Integer.parseInt(txtId.getText()));

            boolean sucesso = carroService.atualizarCarro(carro);

            if (sucesso) {
                DialogUtil.showInfo("Carro atualizado com sucesso!");
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

    // Executa a exclusão do veículo selecionado após confirmação
    @FXML
    private void btnExcluirAction(ActionEvent event) {
        if (txtId.getText() == null || txtId.getText().trim().isEmpty()) {
            DialogUtil.showWarning("Selecione um carro para excluir.");
            return;
        }

        if (!DialogUtil.showConfirmation("Tem certeza de que deseja excluir o veículo selecionado?")) {
            return;
        }

        try {
            int id = Integer.parseInt(txtId.getText());
            boolean sucesso = carroService.excluirCarro(id);

            if (sucesso) {
                DialogUtil.showInfo("Carro excluído com sucesso!");
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

    // Consulta os veículos através da camada de serviço
    @FXML
    private void carregarCarros() {
        ArrayList<CarroDTO> lista = carroService.listarCarros();
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
        configurarColunasTabela();
        configurarValidacaoDeAno();
        configurarSelecaoDeLinha();
        configurarPesquisa();
        carregarCarros();
    }

    // Associa cada coluna da tabela ao atributo correspondente do CarroDTO
    private void configurarColunasTabela() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        colModelo.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        colAnoFabricacao.setCellValueFactory(new PropertyValueFactory<>("anoFabricacao"));
        colPlaca.setCellValueFactory(new PropertyValueFactory<>("placa"));
    }

    // Restringe o campo de ano para aceitar apenas dígitos
    private void configurarValidacaoDeAno() {
        txtAnoFabricacao.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                txtAnoFabricacao.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    // Habilita/desabilita os botões de acordo com a existência de uma linha selecionada
    private void configurarSelecaoDeLinha() {
        tblCarro.getSelectionModel().selectedItemProperty().addListener((obs, selecaoAntiga, novaSelecao) -> {
            boolean temSelecao = novaSelecao != null;
            btnEditar.setDisable(!temSelecao);
            btnExcluir.setDisable(!temSelecao);
            btnAdicionar.setDisable(temSelecao);
        });
    }

    // Liga a busca digitada pelo usuário à lista filtrada e ordenável exibida na tabela
    private void configurarPesquisa() {
        FilteredList<CarroDTO> dadosFiltrados = new FilteredList<>(listaCarrosMaster, p -> true);

        txtPesquisa.textProperty().addListener((observable, oldValue, newValue) ->
                dadosFiltrados.setPredicate(carro -> carroService.correspondeATermo(carro, newValue)));

        SortedList<CarroDTO> dadosOrdenados = new SortedList<>(dadosFiltrados);
        dadosOrdenados.comparatorProperty().bind(tblCarro.comparatorProperty());

        tblCarro.setItems(dadosOrdenados);
    }
}
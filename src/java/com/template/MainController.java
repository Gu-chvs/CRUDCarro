package com.template;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;

public class MainController {
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

    @FXML
    private void btnAdicionarAction(ActionEvent event) {
        // 1. Resgata os textos digitados pelo usuario nos campos da tela (Sem ler o ID)
        String marca = txtMarca.getText();
        String modelo = txtModelo.getText();
        String placa = txtPlaca.getText();
        int anoFabricacao = Integer.parseInt(txtAnoFabricacao.getText());

        // 2. Cria o objeto DTO e preenche com os dados coletados da interface
        CarroDTO objcarrodto = new CarroDTO();
        objcarrodto.setMarca(marca);
        objcarrodto.setModelo(modelo);
        objcarrodto.setAnoFabricacao(anoFabricacao);
        objcarrodto.setPlaca(placa);

        // 3. Envia o DTO preenchido para o DAO salvar definitivamente no banco
        CarroDAO objcarrodao = new CarroDAO();
        objcarrodao.inserirCarro(objcarrodto);

        // 4. Atualiza a TableView na tela para mostrar o novo carro cadastrado imediatamente
        carregarCarros();
    }

    @FXML
    private void btnEditarAction(ActionEvent event) {
        // 1. Resgata os dados dos campos de texto da tela
        int id = Integer.parseInt(txtId.getText()); // Captura o ID (obrigatorio para o UPDATE)
        String marca = txtMarca.getText();
        String modelo = txtModelo.getText();
        String placa = txtPlaca.getText();
        int anoFabricacao = Integer.parseInt(txtAnoFabricacao.getText());

        // 2. Cria o objeto DTO e preenche com os novos dados
        CarroDTO objcarrodto = new CarroDTO();
        objcarrodto.setId(id); // Guarda o ID no DTO
        objcarrodto.setMarca(marca);
        objcarrodto.setModelo(modelo);
        objcarrodto.setAnoFabricacao(anoFabricacao);
        objcarrodto.setPlaca(placa);

        // 3. Instancia o DAO e envia o DTO para atualizar no banco de dados
        CarroDAO objcarrodao = new CarroDAO();
        objcarrodao.atualizarCarro(objcarrodto);

        // 4. Atualiza a TableView para mostrar os dados novos na hora
        carregarCarros();
    }

    @FXML
    private void btnExcluirAction(ActionEvent event) {
        // 1. Resgata apenas o ID do campo de texto da tela
        int id = Integer.parseInt(txtId.getText());

        // 2. Instancia o DAO e envia o ID para ser deletado no banco de dados
        CarroDAO objcarrodao = new CarroDAO();
        objcarrodao.excluirCarro(id);

        // 3. Limpa os campos de texto da tela para dar um feedback visual de sumiço
        btnLimparAction(event);

        // 4. Recarrega a TableView para sumir com o carro da tabela na hora
        carregarCarros();
    }

    @FXML
    private void btnLimparAction(ActionEvent event){
        txtId.clear();
        txtMarca.clear();
        txtModelo.clear();
        txtPlaca.clear();
        txtAnoFabricacao.clear();
    }

    @FXML
    private void carregarCarros() {
        CarroDAO objCarroDAO = new CarroDAO();
        ArrayList<CarroDTO> selecionarCarros = objCarroDAO.selecionarCarros();

        // CORRIGIDO: Agora a tabela recebe a lista de fato!
        tblCarro.setItems(FXCollections.observableArrayList(selecionarCarros));
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
        }
    }

    @FXML
    private void initialize() {
        // Mapeamento das colunas com os atributos exatos do DTO
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        colModelo.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        colAnoFabricacao.setCellValueFactory(new PropertyValueFactory<>("anoFabricacao"));
        colPlaca.setCellValueFactory(new PropertyValueFactory<>("placa")); // CORRIGIDO: "placa" minúsculo

        // Carrega as informações assim que a janela abre
        carregarCarros();
    }
}
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

        String marca = txtMarca.getText();
        String modelo = txtModelo.getText();
        String placa = txtPlaca.getText();
        int anoFabricacao = Integer.parseInt(txtAnoFabricacao.getText());

        CarroDTO objCarroDTO = new CarroDTO();
        objCarroDTO.setMarca(marca);
        objCarroDTO.setModelo(modelo);
        objCarroDTO.setAnoFabricacao(anoFabricacao);
        objCarroDTO.setPlaca(placa);

        CarroDAO objcarrodao = new CarroDAO();
        objcarrodao.inserirCarro(objCarroDTO);

        carregarCarros();
    }

    @FXML
    private void btnEditarAction(ActionEvent event) {

        int id = Integer.parseInt(txtId.getText());
        String marca = txtMarca.getText();
        String modelo = txtModelo.getText();
        String placa = txtPlaca.getText();
        int anoFabricacao = Integer.parseInt(txtAnoFabricacao.getText());

        CarroDTO objCarroDTO = new CarroDTO();
        objCarroDTO.setId(id);
        objCarroDTO.setMarca(marca);
        objCarroDTO.setModelo(modelo);
        objCarroDTO.setAnoFabricacao(anoFabricacao);
        objCarroDTO.setPlaca(placa);

        CarroDAO objCarroDAO = new CarroDAO();
        objCarroDAO.atualizarCarro(objCarroDTO);

        carregarCarros();
    }

    @FXML
    private void btnExcluirAction(ActionEvent event) {
        int id = Integer.parseInt(txtId.getText());

        CarroDAO objCarroDAO = new CarroDAO();
        objCarroDAO.excluirCarro(id);

        btnLimparAction(event);

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
        colPlaca.setCellValueFactory(new PropertyValueFactory<>("placa"));

        // Carrega as informações assim que a janela abre
        carregarCarros();
    }
}
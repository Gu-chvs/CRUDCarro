package com.template.util;

import com.template.model.dto.CarroDTO;
import com.template.service.ICarroService;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Utilitário responsável pela configuração de componentes visuais da tabela,
 * incluindo mapeamento de colunas, listeners de seleção e filtros dinâmicos de pesquisa.
 */
public class TableHelper {

    // Associa cada coluna da tabela à propriedade correspondente do DTO
    public static void configurarColunasTabela(
            TableColumn<CarroDTO, Integer> colId,
            TableColumn<CarroDTO, String> colMarca,
            TableColumn<CarroDTO, String> colModelo,
            TableColumn<CarroDTO, Integer> colAnoFabricacao,
            TableColumn<CarroDTO, String> colPlaca
    ) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        colModelo.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        colAnoFabricacao.setCellValueFactory(new PropertyValueFactory<>("anoFabricacao"));
        colPlaca.setCellValueFactory(new PropertyValueFactory<>("placa"));
    }

    // Controla o estado de ativação dos botões da interface conforme a seleção na tabela
    public static void configurarSelecaoDeLinha(
            TableView<CarroDTO> tblCarro,
            Button btnAdicionar,
            Button btnEditar,
            Button btnExcluir
    ) {
        tblCarro.getSelectionModel().selectedItemProperty().addListener((obs, selecaoAntiga, novaSelecao) -> {
            boolean temSelecao = novaSelecao != null;
            btnEditar.setDisable(!temSelecao);
            btnExcluir.setDisable(!temSelecao);
            btnAdicionar.setDisable(temSelecao);
        });
    }

    // Vincula o campo de pesquisa do FXML ao filtro e ordenação da TableView
    public static void configurarPesquisa(
            TextField txtPesquisa,
            TableView<CarroDTO> tblCarro,
            ObservableList<CarroDTO> listaCarrosMaster,
            ICarroService carroService
    ) {
        FilteredList<CarroDTO> dadosFiltrados = new FilteredList<>(listaCarrosMaster, p -> true);

        txtPesquisa.textProperty().addListener((observable, oldValue, newValue) ->
                dadosFiltrados.setPredicate(carro -> carroService.correspondeATermo(carro, newValue)));

        SortedList<CarroDTO> dadosOrdenados = new SortedList<>(dadosFiltrados);
        dadosOrdenados.comparatorProperty().bind(tblCarro.comparatorProperty());

        tblCarro.setItems(dadosOrdenados);
    }
}

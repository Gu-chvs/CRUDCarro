package com.template.main;

import com.template.controller.MainController;
import com.template.model.dao.CarroDAO;
import com.template.model.dao.ICarroDAO;
import com.template.service.CarroService;
import com.template.service.ICarroService;
import com.template.validator.CarroValidador;
import com.template.validator.ICarroValidador;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // 1. Instancia as dependências fora do Controller (Requisito 7)
        ICarroDAO carroDAO = new CarroDAO();
        ICarroService carroService = new CarroService(carroDAO);
        ICarroValidador carroValidador = new CarroValidador();

        // 2. Configura o FXMLLoader com a Fábrica de Controladores (Requisitos 6 e 7)
        URL fxmlLocation = getClass().getResource("/com/template/main.fxml");
        if (fxmlLocation == null) {
            fxmlLocation = getClass().getResource("../main.fxml");
        }

        FXMLLoader loader = new FXMLLoader(fxmlLocation);
        loader.setControllerFactory(controllerClass -> {
            if (controllerClass == MainController.class) {
                // Injeção de dependência através da fábrica de controladores
                return new MainController(carroService, carroValidador);
            }
            try {
                return controllerClass.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Erro ao criar controlador na fábrica: " + e.getMessage(), e);
            }
        });

        Parent root = loader.load();
        Scene scene = new Scene(root, 750, 620);

        primaryStage.setTitle("Sistema de Gestão de Veículos");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
package com.template.model; // Alterado para casar com o mesmo pacote do seu CarroDAO

import java.sql.Connection;
import java.sql.DriverManager; // Gerenciador de bancos
import java.sql.SQLException;

public class Conexao {

    // Configurações da sua URL do PostgreSQL, usuário e senha padrão
    private static final String CONEXAO = "jdbc:postgresql://localhost:5432/ProjetoCarro";
    private static final String USUARIO = "postgres";
    private static final String SENHA = "postgres";

    public Connection conectaBD() {
        try {
            // Abre e retorna a ponte de conexão com o banco
            return DriverManager.getConnection(CONEXAO, USUARIO, SENHA);
        } catch (SQLException e) {
            // Se der erro (ex: banco desligado ou senha errada), joga a exceção na tela
            throw new RuntimeException("Erro ao conectar com o banco de dados: " + e.getMessage(), e);
        }
    }
}
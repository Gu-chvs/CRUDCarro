package com.template.model.dao;

import com.template.model.Conexao;
import com.template.model.dto.CarroDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.template.util.DialogUtil.showError;

public class CarroDAO implements ICarroDAO {

    private static final Logger logger = Logger.getLogger(CarroDAO.class.getName());

    @Override
    public boolean inserirCarro(CarroDTO carro) {
        String sql = "INSERT INTO carros (marca, modelo, ano_fabricacao, placa) VALUES (?, ?, ?, ?)";
        try (Connection conexao = new Conexao().conectaBD();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, carro.getMarca());
            ps.setString(2, carro.getModelo());
            ps.setInt(3, carro.getAnoFabricacao());
            ps.setString(4, carro.getPlaca());
            ps.execute();
            return true;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erro ao inserir o carro", e);
            showError("Erro ao inserir o carro!");
            return false;
        }
    }

    @Override
    public ArrayList<CarroDTO> selecionarCarros() {
        String sql = "SELECT * FROM carros";
        ArrayList<CarroDTO> listaCarros = new ArrayList<>();

        try (Connection conexao = new Conexao().conectaBD();
             PreparedStatement ps = conexao.prepareStatement(sql);
             ResultSet resultado = ps.executeQuery()) {

            while (resultado.next()) {
                CarroDTO carro = new CarroDTO();
                carro.setId(resultado.getInt("id"));
                carro.setMarca(resultado.getString("marca"));
                carro.setModelo(resultado.getString("modelo"));
                carro.setAnoFabricacao(resultado.getInt("ano_fabricacao"));
                carro.setPlaca(resultado.getString("placa"));
                listaCarros.add(carro);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erro ao selecionar carros", e);
            showError("Erro ao selecionar o carro!");
        }
        return listaCarros;
    }

    @Override
    public boolean atualizarCarro(CarroDTO carro) {
        String sql = "UPDATE carros SET marca = ?, modelo = ?, ano_fabricacao = ?, placa = ? WHERE id = ?";
        try (Connection conexao = new Conexao().conectaBD();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, carro.getMarca());
            ps.setString(2, carro.getModelo());
            ps.setInt(3, carro.getAnoFabricacao());
            ps.setString(4, carro.getPlaca());
            ps.setInt(5, carro.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erro ao atualizar o carro", e);
            showError("Erro ao atualizar o carro!");
            return false;
        }
    }

    @Override
    public boolean excluirCarro(int id) {
        String sql = "DELETE FROM carros WHERE id = ?";
        try (Connection conexao = new Conexao().conectaBD();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erro ao excluir o carro", e);
            showError("Erro ao excluir o carro!");
            return false;
        }
    }
}

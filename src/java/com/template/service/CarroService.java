package com.template.service;

import com.template.model.dao.CarroDAO;
import com.template.model.dao.ICarroDAO;
import com.template.model.dto.CarroDTO;
import com.template.validator.CarroValidador;
import com.template.validator.ICarroValidador;

import java.util.ArrayList;

public class CarroService implements ICarroService {

    private final ICarroDAO carroDAO;
    private final ICarroValidador validador;

    public CarroService(ICarroDAO carroDAO, ICarroValidador validador) {
        this.carroDAO = carroDAO;
        this.validador = validador;
    }

    public CarroService(ICarroDAO carroDAO) {
        this(carroDAO, new CarroValidador());
    }

    public CarroService() {
        this(new CarroDAO(), new CarroValidador());
    }

    @Override
    public CarroDTO criarComDados(String marca, String modelo, String ano, String placa) {
        validarCampos(marca, modelo, ano, placa);

        CarroDTO carro = new CarroDTO();
        carro.setMarca(marca != null ? marca.trim() : "");
        carro.setModelo(modelo != null ? modelo.trim() : "");
        carro.setAnoFabricacao(Integer.parseInt(ano.trim()));
        carro.setPlaca(placa != null ? placa.trim() : "");
        return carro;
    }

    @Override
    public void cadastrar(String marca, String modelo, String ano, String placa) {
        CarroDTO carro = criarComDados(marca, modelo, ano, placa);
        cadastrarCarro(carro);
    }

    @Override
    public void atualizar(String idStr, String marca, String modelo, String ano, String placa) {
        int id = validarEConverterId(idStr);
        CarroDTO carro = criarComDados(marca, modelo, ano, placa);
        carro.setId(id);
        atualizarCarro(carro);
    }

    @Override
    public void excluir(String idStr) {
        int id = validarEConverterId(idStr);
        excluirCarro(id);
    }

    @Override
    public boolean cadastrarCarro(CarroDTO carro) {
        if (carro == null) {
            throw new IllegalArgumentException("Dados do veículo não fornecidos.");
        }
        boolean sucesso = carroDAO.inserirCarro(carro);
        if (!sucesso) {
            throw new RuntimeException("Não foi possível cadastrar o carro no banco de dados.");
        }
        return true;
    }

    @Override
    public boolean atualizarCarro(CarroDTO carro) {
        if (carro == null) {
            throw new IllegalArgumentException("Dados do veículo não fornecidos.");
        }
        if (carro.getId() <= 0) {
            throw new IllegalArgumentException("ID do veículo inválido para atualização.");
        }
        boolean sucesso = carroDAO.atualizarCarro(carro);
        if (!sucesso) {
            throw new RuntimeException("Não foi possível atualizar o carro no banco de dados.");
        }
        return true;
    }

    @Override
    public boolean excluirCarro(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID do veículo inválido para exclusão.");
        }
        boolean sucesso = carroDAO.excluirCarro(id);
        if (!sucesso) {
            throw new RuntimeException("Não foi possível excluir o carro do banco de dados.");
        }
        return true;
    }

    @Override
    public ArrayList<CarroDTO> listarCarros() {
        return carroDAO.selecionarCarros();
    }

    @Override
    public boolean correspondeATermo(CarroDTO carro, String termo) {
        if (termo == null || termo.trim().isEmpty()) {
            return true;
        }

        String termoBusca = termo.toLowerCase().trim();

        return (carro.getMarca() != null && carro.getMarca().toLowerCase().contains(termoBusca))
                || (carro.getModelo() != null && carro.getModelo().toLowerCase().contains(termoBusca))
                || (carro.getPlaca() != null && carro.getPlaca().toLowerCase().contains(termoBusca))
                || String.valueOf(carro.getId()).contains(termoBusca);
    }

    private void validarCampos(String marca, String modelo, String ano, String placa) {
        if (validador != null) {
            validador.validarCampos(marca, modelo, ano, placa);
        }
    }

    private int validarEConverterId(String idStr) {
        if (idStr == null || idStr.trim().isEmpty()) {
            throw new IllegalArgumentException("Selecione um carro para prosseguir com a operação.");
        }
        try {
            int id = Integer.parseInt(idStr.trim());
            if (id <= 0) {
                throw new IllegalArgumentException("O ID do veículo selecionado é inválido.");
            }
            return id;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("O ID do veículo deve ser numérico.");
        }
    }
}
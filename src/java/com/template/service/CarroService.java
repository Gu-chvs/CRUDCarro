package com.template.service;

import com.template.model.dao.CarroDAO;
import com.template.model.dao.ICarroDAO;
import com.template.model.dto.CarroDTO;

import java.util.ArrayList;

public class CarroService implements ICarroService {

    private final ICarroDAO carroDAO;

    public CarroService(ICarroDAO carroDAO) {
        this.carroDAO = carroDAO;
    }

    public CarroService() {
        this(new CarroDAO());
    }

    @Override
    public CarroDTO criarComDados(String marca, String modelo, String ano, String placa) {
        CarroDTO carro = new CarroDTO();
        carro.setMarca(marca.trim());
        carro.setModelo(modelo.trim());
        carro.setAnoFabricacao(Integer.parseInt(ano.trim()));
        carro.setPlaca(placa.trim());
        return carro;
    }

    @Override
    public boolean cadastrarCarro(CarroDTO carro) {
        return carroDAO.inserirCarro(carro);
    }

    @Override
    public boolean atualizarCarro(CarroDTO carro) {
        return carroDAO.atualizarCarro(carro);
    }

    @Override
    public boolean excluirCarro(int id) {
        return carroDAO.excluirCarro(id);
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

        return carro.getMarca().toLowerCase().contains(termoBusca)
                || carro.getModelo().toLowerCase().contains(termoBusca)
                || (carro.getPlaca() != null && carro.getPlaca().toLowerCase().contains(termoBusca))
                || String.valueOf(carro.getId()).contains(termoBusca);
    }
}
package com.template.service;

import com.template.model.CarroDTO;

public class CarroService {

    // Monta um CarroDTO a partir dos textos do formulário, tratando trim e parse do ano
    public CarroDTO criarComDados(String marca, String modelo, String ano, String placa) {
        CarroDTO carro = new CarroDTO();
        carro.setMarca(marca.trim());
        carro.setModelo(modelo.trim());
        carro.setAnoFabricacao(Integer.parseInt(ano.trim()));
        carro.setPlaca(placa.trim());
        return carro;
    }

    // Verifica se um carro corresponde a um termo de busca (marca, modelo, placa ou id)
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
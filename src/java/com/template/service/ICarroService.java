package com.template.service;

import com.template.model.dto.CarroDTO;
import java.util.ArrayList;

public interface ICarroService {
    CarroDTO criarComDados(String marca, String modelo, String ano, String placa);
    boolean cadastrarCarro(CarroDTO carro);
    boolean atualizarCarro(CarroDTO carro);
    boolean excluirCarro(int id);
    ArrayList<CarroDTO> listarCarros();
    boolean correspondeATermo(CarroDTO carro, String termo);
}

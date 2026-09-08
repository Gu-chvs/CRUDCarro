package com.template.model.dao;

import com.template.model.dto.CarroDTO;
import java.util.ArrayList;

public interface ICarroDAO {
    boolean inserirCarro(CarroDTO carro);
    ArrayList<CarroDTO> selecionarCarros();
    boolean atualizarCarro(CarroDTO carro);
    boolean excluirCarro(int id);
}

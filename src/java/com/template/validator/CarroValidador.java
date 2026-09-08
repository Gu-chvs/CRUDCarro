package com.template.validator;

import com.template.util.DialogUtil;
import java.util.ArrayList;
import java.util.List;

public class CarroValidador implements ICarroValidador {

    @Override
    public boolean validarCampos(String marca, String modelo, String ano) {
        List<Validador<String>> validadores = new ArrayList<>();

        // Armazena as validações dos diferentes campos
        validadores.add(new CamposObrigatoriosValidador("Marca", marca));
        validadores.add(new CamposObrigatoriosValidador("Modelo", modelo));
        validadores.add(new CamposObrigatoriosValidador("Ano", ano));
        validadores.add(new ModeloValidador(modelo));
        validadores.add(new AnoValidador(ano));

        // Estrutura foreach obrigatória percorrendo os validadores
        for (Validador<String> validador : validadores) {
            if (!validador.validar(validador.getValor())) {
                DialogUtil.showWarning(validador.getMensagemErro());
                return false;
            }
        }

        return true;
    }
}

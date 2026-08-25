package com.template.validator;

import com.template.util.DialogUtil;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

public class CarroValidator {

    // Valida os campos do veículo e exibe alertas de aviso para cada tipo de falha
    public boolean validarCampos(String marca, String modelo, String ano) {
        List<Validador<String>> validadores = new ArrayList<>();

        validadores.add(new CampoObrigatorioValidador("Marca", marca));
        validadores.add(new CampoObrigatorioValidador("Modelo", modelo));
        validadores.add(new CampoObrigatorioValidador("Ano", ano));
        validadores.add(new ModeloValidador(modelo));

        for (Validador<String> validador : validadores) {
            if (!validador.validar(validador.getValor())) {
                DialogUtil.showWarning(validador.getMessagemError());
                return false;
            }
        }

        if (!ano.trim().matches("\\d+")) {
            DialogUtil.showWarning("O Ano de Fabricação deve conter apenas números!");
            return false;
        }

        int anoInt = Integer.parseInt(ano.trim());
        int anoLimite = Year.now().getValue() + 1;

        if (anoInt < 1886 || anoInt > anoLimite) {
            DialogUtil.showWarning("O Ano de Fabricação deve ser entre 1886 e " + anoLimite + "!");
            return false;
        }

        return true;
    }
}
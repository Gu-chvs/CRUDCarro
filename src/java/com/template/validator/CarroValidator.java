package com.template.validator;

import com.template.util.DialogUtil;
import java.time.Year;

public class CarroValidator {

    // Valida os campos do veículo e exibe alertas de aviso para cada tipo de falha
    public boolean validarCampos(String marca, String modelo, String ano) {
        if (marca == null || marca.trim().isEmpty() ||
                modelo == null || modelo.trim().isEmpty() ||
                ano == null || ano.trim().isEmpty()) {

            DialogUtil.showWarning("Os campos Marca, Modelo e Ano são obrigatórios!");
            return false;
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
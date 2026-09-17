package com.template.validator;

import java.time.Year;

public class AnoValidador implements Validador<String> {
    private final String ano;
    private String mensagemErro;

    public AnoValidador(String ano) {
        this.ano = ano;
    }

    @Override
    public boolean validar(String valor) {
        if (this.ano == null || this.ano.trim().isEmpty()) {
            this.mensagemErro = "O campo Ano de Fabricação deve ser preenchido.";
            return false;
        }

        if (!this.ano.trim().matches("^\\d+$")) {
            this.mensagemErro = "O Ano de Fabricação deve conter apenas números!";
            return false;
        }

        try {
            int anoInt = Integer.parseInt(this.ano.trim());
            int anoLimite = Year.now().getValue() + 1;

            if (anoInt < 1886 || anoInt > anoLimite) {
                this.mensagemErro = "O Ano de Fabricação deve ser entre 1886 e " + anoLimite + "!";
                return false;
            }
        } catch (NumberFormatException e) {
            this.mensagemErro = "O Ano de Fabricação informado é inválido!";
            return false;
        }

        return true;
    }

    @Override
    public String getMensagemErro() {
        return mensagemErro != null ? mensagemErro : "Ano inválido.";
    }

    @Override
    public String getValor() {
        return ano;
    }
}

package com.template.validator;

public interface ICarroValidador {
    boolean validarCampos(String marca, String modelo, String ano, String placa);
    boolean validarCampos(String marca, String modelo, String ano);
}

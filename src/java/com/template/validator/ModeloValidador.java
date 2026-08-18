package com.template.validator;

public class ModeloValidador implements Validador<String> {
    private final String modelo; // Armazena o modelo a ser validado

    public ModeloValidador(String modelo) {
        this.modelo = modelo;
    }

    @Override
    public boolean validar(String valor) {
        return this.modelo != null && this.modelo.trim().matches("[A-Za-zÀ-ÿ ]+");
    }

    @Override
    public String getMessagemError() {
        return "O campo Modelo não pode conter números.";
    }

    @Override
    public String getValor() {
        return modelo;
    }
}
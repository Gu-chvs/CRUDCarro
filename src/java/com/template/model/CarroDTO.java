package com.template.model;

public class CarroDTO {
    private int id;
    private String marca;
    private String modelo;
    private Integer anoFabricacao;
    private String placa;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Integer getAnoFabricacao() {
        return anoFabricacao;
    }

    public void setAnoFabricacao(Integer anoFabricacao) {
        this.anoFabricacao = anoFabricacao;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    // Monta um CarroDTO a partir dos textos do formulário, tratando trim e parse do ano
    public static CarroDTO criarComDados(String marca, String modelo, String ano, String placa) {
        CarroDTO carro = new CarroDTO();
        carro.setMarca(marca.trim());
        carro.setModelo(modelo.trim());
        carro.setAnoFabricacao(Integer.parseInt(ano.trim()));
        carro.setPlaca(placa.trim());
        return carro;
    }

    // Verifica se este carro corresponde a um termo de busca (marca, modelo, placa ou id)
    public boolean correspondeATermo(String termo) {
        if (termo == null || termo.trim().isEmpty()) {
            return true;
        }

        String termoBusca = termo.toLowerCase().trim();

        return marca.toLowerCase().contains(termoBusca)
                || modelo.toLowerCase().contains(termoBusca)
                || (placa != null && placa.toLowerCase().contains(termoBusca))
                || String.valueOf(id).contains(termoBusca);
    }
}
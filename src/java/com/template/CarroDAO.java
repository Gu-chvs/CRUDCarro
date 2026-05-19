package com.template;

public class CarroDAO {
    public void inserirCarro(CarroDTO carro) {
        String sql = "INSERT INTO carros (marca, modelo, ano_fabricacao, placa) VALUES (?, ?, ?, ?)";

        // Utilizando try-with-resources para fechamento automático de recursos
        try (Connection conexao = new Conexao().conectaBD();
             PreparedStatement ps = conexao.prepareStatement(sql)) { // Prepara o comando SQL no banco e cria o 'stmt' para preenchermos os dados

            ps.setString(1, carro.getMarca());
            ps.setString(2, carro.getModelo());
            ps.setInt(3, carro.getAnoFabricacao());
            ps.setString(4, carro.getPlaca());

            ps.execute();
            System.out.println("-> SUCESSO: Carro cadastrado no sistema!");

        } catch (SQLException e) {
            System.err.println("-> ERRO AO INSERIR CARRO: " + e.getMessage());
        }
    }

    public void selecionarCarros() {
        String sql = "SELECT * FROM carros";

        try (Connection conexao = new Conexao().conectaBD();
             PreparedStatement ps = conexao.prepareStatement(sql);
             ResultSet resultado = ps.executeQuery()) {

            System.out.println("\n--- LISTA DE CARROS CADASTRADOS ---");
            boolean possuiRegistros = false;

            while (resultado.next()) {
                possuiRegistros = true;
                int id = resultado.getInt("id");
                String marca = resultado.getString("marca");
                String modelo = resultado.getString("modelo");
                int ano = resultado.getInt("ano_fabricacao");
                String placa = resultado.getString("placa");

                System.out.printf("ID: %d | Marca: %s | Modelo: %s | Ano: %d | Placa: %s\n",
                        id, marca, modelo, ano, placa);
            }

            if (!possuiRegistros) {
                System.out.println("Nenhum carro encontrado no banco de dados.");
            }
            System.out.println("-----------------------------------");

        } catch (SQLException e) {
            System.err.println("-> ERRO AO BUSCAR CARROS: " + e.getMessage());
        }
    }

    public void atualizarCarro(CarroDTO carro) {
        String sql = "UPDATE carros SET marca = ?, modelo = ?, ano_fabricacao = ?, placa = ? WHERE id = ?";

        try (Connection conexao = new Conexao().conectaBD();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, carro.getMarca());
            ps.setString(2, carro.getModelo());
            ps.setInt(3, carro.getAnoFabricacao());
            ps.setString(4, carro.getPlaca());
            ps.setInt(5, carro.getId());

            int linhasAfetadas = ps.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("-> SUCESSO: Dados do carro atualizados!");
            } else {
                System.out.println("-> AVISO: Nenhum carro encontrado com o ID informado.");
            }

        } catch (SQLException e) {
            System.err.println("-> ERRO AO ATUALIZAR CARRO: " + e.getMessage());
        }
    }

    public void excluirCarro(int id) {
        String sql = "DELETE FROM carros WHERE id = ?";

        try (Connection conexao = new Conexao().conectaBD();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setInt(1, id);
            int linhasAfetadas = ps.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("-> SUCESSO: Carro removido do sistema!");
            } else {
                System.out.println("-> AVISO: Nenhum carro encontrado com o ID informado.");
            }

        } catch (SQLException e) {
            System.err.println("-> ERRO AO EXCLUIR CARRO: " + e.getMessage());
        }
    }
}

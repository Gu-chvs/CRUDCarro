package com.template;

/**
 * Classe responsavel pelas operacoes de banco de dados (CRUD) da entidade Carro.
 * Adaptada para retornar colecoes de dados para a interface grafica.
 */
public class CarroDAO {

    // Inicializa o Logger oficial do Java para registrar mensagens e erros de sistema
    private static final Logger LOGGER = Logger.getLogger(CarroDAO.class.getName());

    public void inserirCarro(CarroDTO carro) {
        String sql = "INSERT INTO carros (marca, modelo, ano_fabricacao, placa) VALUES (?, ?, ?, ?)";

        try (Connection conexao = new Conexao().conectaBD();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, carro.getMarca());
            ps.setString(2, carro.getModelo());
            ps.setInt(3, carro.getAnoFabricacao());
            ps.setString(4, carro.getPlaca());

            ps.execute();
            LOGGER.log(Level.INFO, "Carro cadastrado com sucesso! Placa: {0}", carro.getPlaca());

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao inserir carro no banco de dados", e);
        }
    }

    /**
     * Busca todos os carros do banco de dados e os retorna em uma lista.
     * Ideal para preencher componentes visuais como TableView.
     */
    public List<CarroDTO> selecionarCarros() {
        String sql = "SELECT * FROM carros";
        List<CarroDTO> listaCarros = new ArrayList<>();

        try (Connection conexao = new Conexao().conectaBD();
             PreparedStatement ps = conexao.prepareStatement(sql);
             ResultSet resultado = ps.executeQuery()) { // Executa a busca (SELECT) no banco e guarda a tabela de respostas na variavel 'resultado'

            while (resultado.next()) {
                CarroDTO carro = new CarroDTO();
                carro.setId(resultado.getInt("id"));
                carro.setMarca(resultado.getString("marca"));
                carro.setModelo(resultado.getString("modelo"));
                carro.setAnoFabricacao(resultado.getInt("ano_fabricacao"));
                carro.setPlaca(resultado.getString("placa"));

                // Adiciona o carro preenchido para dentro da nossa lista na memoria
                listaCarros.add(carro);
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar a lista de carros no banco", e);
        }

        return listaCarros;
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
                LOGGER.log(Level.INFO, "Dados do carro com ID {0} atualizados.", carro.getId());
            } else {
                LOGGER.log(Level.WARNING, "Nenhum carro encontrado para atualizar com o ID: {0}", carro.getId());
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao atualizar dados do carro", e);
        }
    }

    public void excluirCarro(int id) {
        String sql = "DELETE FROM carros WHERE id = ?";

        try (Connection conexao = new Conexao().conectaBD();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setInt(1, id);
            int linhasAfetadas = ps.executeUpdate();

            if (linhasAfetadas > 0) {
                LOGGER.log(Level.INFO, "Carro com ID {0} removido do sistema.", id);
            } else {
                LOGGER.log(Level.WARNING, "Nenhum carro encontrado para exclusao com o ID: {0}", id);
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao tentar excluir carro", e);
        }
    }
}
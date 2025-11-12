package br.com.mercadinhoprovidence.dao;

import br.com.mercadinhoprovidence.config.ConexaoMySQL;
import br.com.mercadinhoprovidence.model.ItemVenda;
import br.com.mercadinhoprovidence.model.Produto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemVendaDao {

    private ItemVenda mapToItemVenda(ResultSet rs) throws SQLException {
        ItemVenda itemVenda = new ItemVenda(
                rs.getInt("idItemVenda"),
                rs.getInt("idVenda"),
                rs.getDouble("quantidade_ou_peso"),
                rs.getDouble("precoUnitarioVenda"),
                rs.getDouble("totalItem"),
                null
        );
        return itemVenda;
    }

    public List<ItemVenda> buscarItensPorIdVenda(Integer idVenda) {
        List<ItemVenda> itens = new ArrayList<>();
        String sql = "SELECT idItemVenda, idVenda, idProduto, quantidade_ou_peso, precoUnitarioVenda, totalItem FROM ITENS_VENDA WHERE idVenda = ?";
        ProdutoDao produtoDAO = new ProdutoDao();

        try (Connection connection = ConexaoMySQL.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idVenda);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ItemVenda item = mapToItemVenda(rs);
                    item.setProduto(produtoDAO.buscarProdutoPorId(rs.getInt("idProduto")));
                    itens.add(item);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar itens de venda por idVenda: " + e.getMessage());
        }

        return itens;
    }

    // --- Método 1: Inserir um único ItemVenda (Uso unitário/isolado) ---
    public void inserir(ItemVenda itemVenda) throws SQLException {
        try (Connection connection = ConexaoMySQL.getConnection()) {
            inserirComConexao(itemVenda, connection);
        }
    }

    public void inserirComConexao(ItemVenda itemVenda, Connection conn) throws SQLException {
        String sql = "INSERT INTO ITENS_VENDA (idVenda, idProduto, quantidade_ou_peso, precoUnitarioVenda, totalItem) VALUES (?, ?, ?, ?, ?)";
        ResultSet generatedKeys = null;
      try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, itemVenda.getIdVenda());

            if (itemVenda.getProduto() == null || itemVenda.getProduto().getIdProduto() == null) {
                throw new SQLException("Produto ou ID do Produto não associado ao ItemVenda para inserção.");
            }
            stmt.setInt(2, itemVenda.getProduto().getIdProduto());
            stmt.setDouble(3, itemVenda.getQuantidadeOuPeso());
            stmt.setDouble(4, itemVenda.getPrecoUnitarioVenda());
            stmt.setDouble(5, itemVenda.getTotalItem());
            stmt.executeUpdate();

            generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                itemVenda.setIdItemVenda(generatedKeys.getInt(1));
            }
        } finally {
            try { if (generatedKeys != null) generatedKeys.close(); } catch (SQLException e) { /* ignore */ }
        }
    }
}

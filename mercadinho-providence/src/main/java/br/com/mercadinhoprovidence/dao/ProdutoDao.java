package br.com.mercadinhoprovidence.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.com.mercadinhoprovidence.config.ConexaoMySQL;
import br.com.mercadinhoprovidence.model.ItemVenda;
import br.com.mercadinhoprovidence.model.Produto;
import br.com.mercadinhoprovidence.model.enums.Categoria;

public class ProdutoDao {

    public ProdutoDao() {
    }

    private Produto mapToProduto(ResultSet rs) throws SQLException {
        java.sql.Date sqlDate = rs.getDate("validade");
        LocalDate validade = (sqlDate != null) ? sqlDate.toLocalDate() : null;

        Produto p = new Produto(
                rs.getInt("idProduto"),
                rs.getInt("idEstoque"),
                rs.getString("codigoDeBarras"),
                rs.getString("nome"),
                rs.getBoolean("ativo"),
                rs.getString("descricao"),
                rs.getDouble("precoCusto"),
                rs.getDouble("precoVenda"),
                validade,
                rs.getInt("quantidadeOuPesoEmEstoque"),
                rs.getDouble("desconto"),
                Categoria.fromString(rs.getString("categoria")));

        return p;
    }

        public boolean inserir(Produto produto) {
        String sql = """
            INSERT INTO Produto (
                idEstoque, codigoDeBarras, nome, ativo, descricao,
                precoCusto, precoVenda, validade, quantidadeOuPesoEmEstoque,
                desconto, categoria
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = ConexaoMySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, produto.getIdEstoque());
            stmt.setString(2, produto.getCodigoDeBarras());
            stmt.setString(3, produto.getNome());
            stmt.setBoolean(4, produto.getAtivo());

            if (produto.getDescricao() != null)
                stmt.setString(5, produto.getDescricao());
            else
                stmt.setNull(5, Types.VARCHAR);

            if (produto.getPrecoCusto() != null)
                stmt.setDouble(6, produto.getPrecoCusto());
            else
                stmt.setNull(6, Types.DOUBLE);

            stmt.setDouble(7, produto.getPrecoVenda());

            if (produto.getValidade() != null)
                stmt.setDate(8, Date.valueOf(produto.getValidade()));
            else
                stmt.setNull(8, Types.DATE);

            stmt.setInt(9, produto.getQuantidadeOuPesoEmEstoque());

            if (produto.getDesconto() != null)
                stmt.setDouble(10, produto.getDesconto());
            else
                stmt.setNull(10, Types.DOUBLE);

            stmt.setString(11, produto.getCategoria().toString());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao inserir produto: " + e.getMessage());
            return false;
        }
    }

      // --- BUSCAR PRODUTO POR ID ---
    public Produto buscarProdutoPorId(Integer idProduto) {
        String sql = """
            SELECT * FROM Produto WHERE idProduto = ?
        """;

        try (Connection conn = ConexaoMySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idProduto);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapToProduto(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar produto por ID: " + e.getMessage());
        }
        return null;
    }

    // --- BUSCAR POR CÓDIGO DE BARRAS ---
    public Produto buscarPorCodigoDeBarras(String codigo) {
        String sql = """
            SELECT * FROM Produto WHERE codigoDeBarras = ?
        """;

        try (Connection conn = ConexaoMySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, codigo);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapToProduto(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar produto por código de barras: " + e.getMessage());
        }
        return null;
    }

    // --- LISTAR TODOS ---
    public List<Produto> listarTodos() {
        List<Produto> lista = new ArrayList<>();
        String sql = "SELECT * FROM Produto";

        try (Connection conn = ConexaoMySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapToProduto(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar produtos: " + e.getMessage());
        }
        return lista;
    }

    // --- ATUALIZAR PRODUTO ---
    public Boolean atualizar(Produto produto) {
        String sql = """
            UPDATE Produto
            SET idEstoque = ?, codigoDeBarras = ?, nome = ?, ativo = ?, descricao = ?,
                precoCusto = ?, precoVenda = ?, validade = ?, quantidadeOuPesoEmEstoque = ?,
                desconto = ?, categoria = ?
            WHERE idProduto = ?
        """;

        try (Connection conn = ConexaoMySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, produto.getIdEstoque());
            stmt.setString(2, produto.getCodigoDeBarras());
            stmt.setString(3, produto.getNome());
            stmt.setBoolean(4, produto.getAtivo());
            stmt.setString(5, produto.getDescricao());
            stmt.setDouble(6, produto.getPrecoCusto());
            stmt.setDouble(7, produto.getPrecoVenda());

            if (produto.getValidade() != null)
                stmt.setDate(8, Date.valueOf(produto.getValidade()));
            else
                stmt.setNull(8, Types.DATE);

            stmt.setInt(9, produto.getQuantidadeOuPesoEmEstoque());
            stmt.setDouble(10, produto.getDesconto());
            stmt.setString(11, produto.getCategoria().toString());
            stmt.setInt(12, produto.getIdProduto());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar produto: " + e.getMessage());
            return false;
        }
    }

    // --- DELETAR PRODUTO ---
    public Boolean deletar(Integer id) {
        String sql = "DELETE FROM Produto WHERE idProduto = ?";

        try (Connection conn = ConexaoMySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao deletar produto: " + e.getMessage());
            return false;
        }
    }

    // --- SUBTRAIR ESTOQUE ---
    public void subtrairEstoque(Integer idProduto, Double quantidadeVendida) {
        String sql = """
            UPDATE Produto
            SET quantidadeOuPesoEmEstoque = quantidadeOuPesoEmEstoque - ?
            WHERE idProduto = ?
        """;

        try (Connection conn = ConexaoMySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, quantidadeVendida);
            stmt.setInt(2, idProduto);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao subtrair estoque: " + e.getMessage());
        }
    }

    // --- ATUALIZAR QUANTIDADE E STATUS ---
    public void atualizarProduto(Produto produto) {
        String sql = """
            UPDATE Produto
            SET quantidadeOuPesoEmEstoque = ?, ativo = ?
            WHERE idProduto = ?
        """;

        try (Connection conn = ConexaoMySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, produto.getQuantidadeOuPesoEmEstoque());
            stmt.setBoolean(2, produto.getAtivo());
            stmt.setInt(3, produto.getIdProduto());
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar produto (quantidade/status): " + e.getMessage());
        }
    }
    public void atualizarEstoqueComConexao(ItemVenda itemVenda, Connection conn) throws SQLException {
        String sql = """
        UPDATE Produto
        SET quantidadeOuPesoEmEstoque = quantidadeOuPesoEmEstoque - ?
        WHERE idProduto = ?
    """;

        // Usa a conexão passada para manter a transação (NÃO abre uma nova)
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            // itemVenda.getQuantidadeOuPeso() é a quantidade/peso vendido
            stmt.setDouble(1, itemVenda.getQuantidadeOuPeso());
            stmt.setInt(2, itemVenda.getProduto().getIdProduto());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                // Opcional: Lançar erro se o produto não foi encontrado (produto deletado no meio da venda?)
                // throw new SQLException("Produto com ID " + itemVenda.getProduto().getIdProduto() + " não encontrado para baixa de estoque.");
            }
        }
    }
}


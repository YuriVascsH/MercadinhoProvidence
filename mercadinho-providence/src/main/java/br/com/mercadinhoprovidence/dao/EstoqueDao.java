package br.com.mercadinhoprovidence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.mercadinhoprovidence.config.ConexaoMySQL;
import br.com.mercadinhoprovidence.model.Estoque;

public class EstoqueDao {

    public EstoqueDao() {
    }

    /**
     * Método que inseri o estoque
     * 
     * @param estoque
     */
    public void inserir(Estoque estoque) {
        String sql = "INSERT INTO Estoque () VALUES ()";
        try (Connection connection = ConexaoMySQL.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao inserir estoque: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 
     * @return
     */
    public List<Estoque> listarTodos() {
        List<Estoque> lista = new ArrayList<>();
        String sql = "SELECT idEstoque FROM Estoque";

        try (Connection connection = ConexaoMySQL.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Estoque e = new Estoque(rs.getInt("idEstoque"));
                lista.add(e);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar estoques: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * 
     * @param id
     * @return
     */
    public Estoque buscarPorId(Integer id) {
        String sql = "SELECT idEstoque FROM Estoque WHERE idEstoque = ?";
        try (Connection connection = ConexaoMySQL.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Estoque(rs.getInt("idEstoque"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar por ID estoque: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 
     * @param idEstoque
     * @return
     */
    public int produtosCadastradosNoEstoque(Integer idEstoque) {
        String sql = "SELECT total FROM TotalProdutosCadastrados WHERE id = 1";
        try (Connection connection = ConexaoMySQL.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}

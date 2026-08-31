package br.com.mercadinhoprovidence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException; // Importação correta do Enum
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import br.com.mercadinhoprovidence.config.ConexaoMySQL;
import br.com.mercadinhoprovidence.model.ItemVenda;
import br.com.mercadinhoprovidence.model.Venda;
import br.com.mercadinhoprovidence.model.enums.Forma;

public class VendaDao {

        // Dependências injetadas (Melhor prática para testes e modularidade)
        private final ItemVendaDao itemVendaDao;
        private final ProductDao produtoDao;

        // Construtor para injeção de dependências
        public VendaDao(ItemVendaDao itemVendaDao, ProductDao produtoDao) {
            this.itemVendaDao = itemVendaDao;
            this.produtoDao = produtoDao;
        }

        // --- Método Auxiliar: Mapeamento do ResultSet ---
        private Venda mapToVenda(ResultSet rs) throws SQLException {
            // Usa o construtor gerado pelo Lombok, passando todos os campos
            Venda venda = new Venda(
                    rs.getInt("id_venda"),
                    rs.getTimestamp("data_hora").toLocalDateTime(),
                    rs.getDouble("valor_subtotal"),
                    rs.getDouble("valor_desconto"),
                    rs.getDouble("valor_total"),
                    rs.getInt("id_funcionario"),
                    rs.getDouble("valor_total_manual"),
                    new ArrayList<>(), // Lista de ItemVenda vazia ao ler
                    rs.getDouble("troco"),
                    rs.getDouble("valor_pago"),
                    Forma.valueOf(rs.getString("forma"))
            );
            // Os setters de troco, valorPago e forma não são mais necessários se você usar o AllArgsConstructor
            return venda;
        }

        // --- Método Principal: Salvar a Venda Completa (Transacional) ---
        public int salvarVendaCompleta(Venda venda) throws SQLException {
            if (venda.getItensVenda() == null || venda.getItensVenda().isEmpty()) {
                // Lançar exceção sem rollback, pois o erro é antes da conexão
                throw new SQLException("Venda sem itens não pode ser salva.");
            }

            // *BOA PRÁTICA: As validações de negócio como valorPago < valorTotal
            // devem idealmente ser feitas na camada Service. Mantidas aqui por agora.*

            int idVendaGerado = -1;
            Connection conn = null;

            try {
                // AQUI INICIA A TRANSAÇÃO
                conn = ConexaoMySQL.getConnection();
                conn.setAutoCommit(false);

                // 1. SALVAR A VENDA na tabela VENDAS
                String sqlInsertVenda = """
                            INSERT INTO VENDAS (
                                data_hora, valor_subtotal, valor_desconto, valor_total, id_funcionario, 
                                valor_total_manual, troco, valor_pago, forma
                            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                        """;

                // Usando try-with-resources para o PreparedStatement
                try (PreparedStatement psVenda = conn.prepareStatement(sqlInsertVenda, Statement.RETURN_GENERATED_KEYS)) {

                    // Parâmetros da Venda
                    psVenda.setTimestamp(1, Timestamp.valueOf(venda.getDataHora()));
                    psVenda.setDouble(2, venda.getValorSubtotal());
                    psVenda.setDouble(3, venda.getValorDesconto());
                    psVenda.setDouble(4, venda.getValorTotal());
                    psVenda.setInt(5, venda.getIdFuncionario());
                    psVenda.setDouble(6, venda.getValorTotalManual());

                    // Parâmetros de Pagamento
                    psVenda.setDouble(7, venda.getTroco());
                    psVenda.setDouble(8, venda.getValorPago());
                    psVenda.setString(9, venda.getForma().toString());

                    int affectedRowsVenda = psVenda.executeUpdate();
                    if (affectedRowsVenda == 0) {
                        throw new SQLException("Falha ao salvar a venda, nenhuma linha afetada.");
                    }

                    // Obtém o ID gerado da Venda
                    try (ResultSet rs = psVenda.getGeneratedKeys()) {
                        if (rs.next()) {
                            idVendaGerado = rs.getInt(1);
                            venda.setIdVenda(idVendaGerado);
                        } else {
                            throw new SQLException("Falha ao obter o ID da venda gerado.");
                        }
                    }
                }

                // 2. SALVAR OS ITENS E DAR BAIXA NO ESTOQUE
                for (ItemVenda item : venda.getItensVenda()) {
                    // 2a. Salva o item na tabela ITENS_VENDA (usando a conexão transacional)
                    item.setIdVenda(idVendaGerado);
                    itemVendaDao.inserirComConexao(item, conn);

                    // 2b. Baixa o estoque (usando o método que requer a conexão transacional)
                    // ESTE MÉTODO DEVE SER IMPLEMENTADO NO ProductDao
                    produtoDao.updateStockWithConnection(item, conn);
                }

                conn.commit(); // TUDO CERTO, CONFIRMA A TRANSAÇÃO
                return idVendaGerado;

            } catch (SQLException e) {
                // Se algo falhou, desfaz as operações
                if (conn != null) {
                    try {
                        System.err.println("Transação será revertida (ROLLBACK).");
                        conn.rollback();
                    } catch (SQLException ex) {
                        System.err.println("Erro ao tentar fazer rollback: " + ex.getMessage());
                    }
                }
                // Relança a exceção
                throw new SQLException("Erro ao salvar a venda completa. Transação desfeita.", e);
            } finally {
                // GARANTE QUE A CONEXÃO É FECHADA E O AUTOCOMMIT É LIGADO NOVAMENTE
                if (conn != null) {
                    try {
                        conn.setAutoCommit(true);
                        conn.close();
                    } catch (SQLException e) {
                        System.err.println("Erro ao fechar a conexão: " + e.getMessage());
                    }
                }
            }
        }
}

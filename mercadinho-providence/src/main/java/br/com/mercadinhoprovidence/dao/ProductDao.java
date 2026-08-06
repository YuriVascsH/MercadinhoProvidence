package br.com.mercadinhoprovidence.dao;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import br.com.mercadinhoprovidence.config.ConexaoMySQL;
import br.com.mercadinhoprovidence.model.Product;

public class ProductDao {

	public ProductDao() {
	}

	private final QueryRunner run = new QueryRunner();

	// 1. Inserir produto

	/**
	 * 
	 * @param p
	 */
	public void save(Product p) {
		if (p.getIdProduto() == null) {
			String sql = "INSERT INTO produtos (nome, codigo_de_barras, descricao, categoria, controla_estoque, preco_venda, preco_custo, quant_ou_peso_em_estoque, desconto, validade, ativo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			try (Connection conn = ConexaoMySQL.getConnection()) {
				// ScalarHandler captura o ID gerado automaticamente
				Long id = run.insert(conn, sql, new ScalarHandler<Long>(), p.getNome(), p.getCodigoDeBarras(),
						p.getDescricao(), p.getCategoria().name(), p.getControlaEstoque(), p.getPrecoVenda(),
						p.getPrecoCusto(), p.getQuantOuPesoEmEstoque(), p.getDesconto(), p.getValidade(),
						p.getActive());
				p.setIdProduto(id.intValue());
			} catch (Exception e) {
				throw new RuntimeException("Erro ao inserir", e);
			}
		} else {
			update(p);
		}
	}

	// 2. Atualizar produto
	/**
	 * 
	 * @param p
	 */
	public void update(Product p) {
		String sql = """
				UPDATE Produto
				SET
				    nome = ?,
				    codigo_de_barras = ?,
				    descricao = ?,
				    categoria = ?,
				    controla_estoque = ?,
				    preco_venda = ?,
				    preco_custo = ?,
				    quant_ou_peso_em_estoque = ?,
				    desconto = ?,
				    validade = ?,
				    ativo = ?
				WHERE idProduto = ?
				""";

		try (Connection conn = ConexaoMySQL.getConnection()) {

			run.update(conn, sql, p.getNome(), p.getCodigoDeBarras(), p.getDescricao(), p.getCategoria().name(),
					p.getControlaEstoque(), p.getPrecoVenda(), p.getPrecoCusto(), p.getQuantOuPesoEmEstoque(),
					p.getDesconto(), p.getValidade(), p.getActive(), p.getIdProduto());

		} catch (Exception e) {
			throw new RuntimeException("Erro ao atualizar", e);
		}
	}

	// 3. Buscar por ID (Mapeia automaticamente para o seu Bean Product!)
	/**
	 * 
	 * @param id
	 * @return
	 */
	public Optional<Product> findById(Integer id) {

		String sql = """
				SELECT
				    idProduto,
				    nome,
				    codigo_de_barras AS codigoDeBarras,
				    descricao,
				    categoria,
				    controla_estoque AS controlaEstoque,
				    preco_venda AS precoVenda,
				    preco_custo AS precoCusto,
				    quant_ou_peso_em_estoque AS quantOuPesoEmEstoque,
				    desconto,
				    validade,
				    ativo AS active
				FROM Produto
				WHERE idProduto = ?
				""";

		try (Connection conn = ConexaoMySQL.getConnection()) {

			Product p = run.query(conn, sql, new BeanHandler<>(Product.class), id);

			return Optional.ofNullable(p);

		} catch (Exception e) {
			throw new RuntimeException("Erro ao buscar", e);
		}
	}

	// 4. Buscar Todos (Retorna uma lista de Beans mapeados)
	/**
	 * 
	 * @return
	 */
	public List<Product> findAll() {

		String sql = """
				SELECT
				    idProduto,
				    nome,
				    codigo_de_barras AS codigoDeBarras,
				    descricao,
				    categoria,
				    controla_estoque AS controlaEstoque,
				    preco_venda AS precoVenda,
				    preco_custo AS precoCusto,
				    quant_ou_peso_em_estoque AS quantOuPesoEmEstoque,
				    desconto,
				    validade,
				    ativo AS active
				FROM Produto
				""";

		try (Connection conn = ConexaoMySQL.getConnection()) {

			return run.query(conn, sql, new BeanListHandler<>(Product.class));

		} catch (Exception e) {
			throw new RuntimeException("Erro ao listar", e);
		}
	}

	// 5. Verificar se existe pelo código
	/**
	 * 
	 * @param code
	 * @return
	 */
	public boolean existsByCode(String code) {

		String sql = "SELECT COUNT(*) FROM Produto WHERE codigo_de_barras = ?";

		try (Connection conn = ConexaoMySQL.getConnection()) {

			Long count = run.query(conn, sql, new ScalarHandler<Long>(), code);

			return count != null && count > 0;

		} catch (Exception e) {
			throw new RuntimeException("Erro ao verificar", e);
		}
	}

	/**
	 * 
	 * @param code
	 * @return
	 */
	public Optional<Product> findByCode(String code) {

		String sql = """
				SELECT
				    idProduto,
				    nome,
				    codigo_de_barras AS codigoDeBarras,
				    descricao,
				    categoria,
				    controla_estoque AS controlaEstoque,
				    preco_venda AS precoVenda,
				    preco_custo AS precoCusto,
				    quant_ou_peso_em_estoque AS quantOuPesoEmEstoque,
				    desconto,
				    validade,
				    ativo AS active
				FROM Produto
				WHERE codigo_de_barras = ?
				""";

		try (Connection conn = ConexaoMySQL.getConnection()) {

			Product p = run.query(conn, sql, new BeanHandler<>(Product.class), code);

			return Optional.ofNullable(p);

		} catch (Exception e) {
			throw new RuntimeException("Erro ao buscar produto pelo código de barras", e);
		}
	}
}

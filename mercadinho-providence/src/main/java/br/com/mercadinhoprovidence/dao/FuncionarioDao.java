package br.com.mercadinhoprovidence.dao;

import br.com.mercadinhoprovidence.config.ConexaoMySQL;
import br.com.mercadinhoprovidence.model.Funcionario;
import br.com.mercadinhoprovidence.model.enums.Cargo;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FuncionarioDao {

    /**
     * Insere um novo funcionário (seja gerente, operador ou outro cargo)
     */
    public boolean inserir(Funcionario f) {
        String sql = """
                    INSERT INTO funcionario (
                        codigoVerificador, cpf, nome, data_nascimento, telefone, email, endereco,
                        dataAdmissao, cargo, salario, senha, ativo
                    ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = ConexaoMySQL.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, f.getCodigoVerificador());
            stmt.setString(2, f.getCpf());
            stmt.setString(3, f.getNome());
            stmt.setDate(4, Date.valueOf(f.getDataNascimento()));
            stmt.setString(5, f.getTelefone());
            stmt.setString(6, f.getEmail());
            stmt.setString(7, f.getEndereco());
            stmt.setDate(8, Date.valueOf(f.getDataAdmissao()));
            stmt.setString(9, f.getCargo().toString());
            stmt.setBigDecimal(10, f.getSalario());
            stmt.setString(11, f.getSenha());
            stmt.setBoolean(12, f.getAtivo());

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        f.setIdFuncionario(rs.getInt(1));
                    }
                }
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Erro ao inserir funcionário: " + e.getMessage());
        }
        return false;
    }

    /**
     * Lista todos os funcionários (independente do cargo)
     */
    public List<Funcionario> listarTodos() {
        List<Funcionario> lista = new ArrayList<>();
        String sql = "SELECT * FROM funcionario";

        try (Connection conn = ConexaoMySQL.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapearFuncionario(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar funcionários: " + e.getMessage());
        }

        return lista;
    }

    /**
     * Lista funcionários filtrando por cargo
     */
    public List<Funcionario> listarPorCargo(Cargo cargo) {
        List<Funcionario> lista = new ArrayList<>();
        String sql = "SELECT * FROM funcionario WHERE cargo = ?";

        try (Connection conn = ConexaoMySQL.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cargo.name());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearFuncionario(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar funcionários por cargo: " + e.getMessage());
        }

        return lista;
    }

    /**
     * Atualiza um funcionário existente
     */
    public void atualizar(Funcionario f) throws SQLException {
        String sql = """
                    UPDATE funcionario
                    SET nome=?, data_nascimento=?, telefone=?, email=?, endereco=?,
                        salario=?, senha=?, ativo=?, cargo=?, codigoVerificador=?, cpf=?, dataAdmissao=?
                    WHERE idFuncionario=?
                """;

        try (Connection conn = ConexaoMySQL.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, f.getNome());
            stmt.setDate(2, Date.valueOf(f.getDataNascimento()));
            stmt.setString(3, f.getTelefone());
            stmt.setString(4, f.getEmail());
            stmt.setString(5, f.getEndereco());
            stmt.setBigDecimal(6, f.getSalario());
            stmt.setString(7, f.getSenha());
            stmt.setBoolean(8, f.getAtivo());
            stmt.setString(9, f.getCargo().name());
            stmt.setInt(10, f.getCodigoVerificador());
            stmt.setString(11, f.getCpf());
            stmt.setDate(12, Date.valueOf(f.getDataAdmissao()));
            stmt.setInt(13, f.getIdFuncionario());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar funcionário: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Exclui um funcionário pelo ID
     */
    public boolean deletar(Integer idFuncionario) {
        String sql = "DELETE FROM funcionario WHERE idFuncionario = ?";

        try (Connection conn = ConexaoMySQL.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idFuncionario);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao deletar funcionário: " + e.getMessage());
            return false;
        }
    }

    /**
     * Busca funcionário por ID
     */
    public Funcionario buscarPorId(Integer id) {
        String sql = "SELECT * FROM funcionario WHERE idFuncionario = ?";

        try (Connection conn = ConexaoMySQL.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearFuncionario(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar funcionário por ID: " + e.getMessage());
        }

        return null;
    }

    /**
     * Busca funcionário por CPF
     */
    public Funcionario buscarPorCpf(String cpf) {
        String sql = "SELECT * FROM funcionario WHERE cpf = ?";

        try (Connection conn = ConexaoMySQL.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearFuncionario(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar funcionário por CPF: " + e.getMessage());
        }

        return null;
    }

    /**
     * Busca funcionário por ID e senha (para login, por exemplo)
     */
    public Funcionario buscarPorIdSenha(Integer id, String senha) {
        String sql = "SELECT * FROM funcionario WHERE idFuncionario = ? AND senha = ?";

        try (Connection conn = ConexaoMySQL.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.setString(2, senha);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearFuncionario(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar funcionário por ID e senha: " + e.getMessage());
        }

        return null;
    }

    /**
     * Busca funcionário por código verificador
     */
    public Funcionario buscarPorCodigoVerificador(String codigoVerificador) {
        String sql = "SELECT * FROM funcionario WHERE codigoVerificador = ?";

        try (Connection conn = ConexaoMySQL.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, codigoVerificador);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearFuncionario(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar funcionário por código verificador: " + e.getMessage());
        }

        return null;
    }

    /**
     * Verifica se já existe CPF cadastrado
     *
     * @param cpf inserido pelo usuario
     */
    public boolean verificarCpfExistente(String cpf) {
        String sql = "SELECT COUNT(*) FROM funcionario WHERE cpf = ?";

        try (Connection conn = ConexaoMySQL.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao verificar CPF existente: " + e.getMessage());
        }

        return false;
    }

    /**
     * Retorna um mapa ID → Nome (útil para combos)
     */
    public Map<Integer, String> buscarTodosComoMap() {
        Map<Integer, String> mapa = new HashMap<>();
        for (Funcionario f : listarTodos()) {
            mapa.put(f.getIdFuncionario(), f.getNome());
        }
        return mapa;
    }

    /**
     * Busca apenas o nome de um funcionário
     *
     * @param idFuncionario fornecido pelo funcionario na sessão de gerenciamento de funcionários
     */
    public String buscarNomePorId(Integer idFuncionario) {
        String sql = "SELECT nome FROM funcionario WHERE idFuncionario = ?";
        try (Connection conn = ConexaoMySQL.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idFuncionario);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("nome");
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar nome do funcionário: " + e.getMessage());
        }
        return null;
    }

    /**
     * Mapeia o resultado SQL em um objeto Funcionario
     */
    private Funcionario mapearFuncionario(ResultSet rs) throws SQLException {
        Funcionario funcionario = new Funcionario();

        funcionario.setIdFuncionario(rs.getInt("idFuncionario"));
        funcionario.setCodigoVerificador(rs.getInt("codigoVerificador"));
        funcionario.setCpf(rs.getString("cpf"));
        funcionario.setNome(rs.getString("nome"));
        funcionario.setDataNascimento(rs.getDate("data_nascimento").toLocalDate());
        funcionario.setTelefone(rs.getString("telefone"));
        funcionario.setEmail(rs.getString("email"));
        funcionario.setEndereco(rs.getString("endereco"));
        funcionario.setDataAdmissao(rs.getDate("dataAdmissao").toLocalDate());
        funcionario.setCargo(Cargo.fromString(rs.getString("cargo")));
        funcionario.setSalario(rs.getBigDecimal("salario"));
        funcionario.setSenha(rs.getString("senha"));
        funcionario.setAtivo(rs.getBoolean("ativo"));

        Timestamp tsUltimaVenda = rs.getTimestamp("ultima_venda");
        if (tsUltimaVenda != null) {
            funcionario.setUltimaVenda(tsUltimaVenda.toLocalDateTime());
        }

        return funcionario;
    }

}

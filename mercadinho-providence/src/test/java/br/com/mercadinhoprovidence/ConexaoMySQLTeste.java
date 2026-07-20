package br.com.mercadinhoprovidence;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import br.com.mercadinhoprovidence.config.ConexaoMySQL;

public class ConexaoMySQLTeste {

    @Test
    public void deveConectarAoBancoComSucesso() {
        try (Connection conn = ConexaoMySQL.getConnection()) {

            assertNotNull(conn, "A conexão não deveria ser nula");

            assertFalse(conn.isClosed(), "A conexão deveria estar ativa/aberta, mas foi encontrada fechada");
            
        } catch (SQLException e) {
            fail("Falha ao conectar ao mysql:" + e.getMessage());
        } catch (RuntimeException e) {
            fail(e);
        }
    }

}

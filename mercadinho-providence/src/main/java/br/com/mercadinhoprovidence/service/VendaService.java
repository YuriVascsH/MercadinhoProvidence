package br.com.mercadinhoprovidence.service;

import java.sql.SQLException;

import br.com.mercadinhoprovidence.dao.VendaDao;
import br.com.mercadinhoprovidence.model.Venda;

public class VendaService {

    private final VendaDao vendaDao;

    public VendaService(VendaDao vendaDao) {
        this.vendaDao = vendaDao;
    }

    /**
     * Valida e salva uma venda completa.
     *
     * @param venda venda que será salva
     * @return ID da venda gerada pelo banco
     */
    public int salvarVenda(Venda venda) {

        validarVenda(venda);

        try {
            return vendaDao.salvarVendaCompleta(venda);

        } catch (SQLException e) {
            throw new RuntimeException("Não foi possível salvar a venda.", e);
        }
    }

    /**
     * Valida as regras básicas antes de enviar a venda ao DAO.
     */
    private void validarVenda(Venda venda) {

        if (venda == null) {
            throw new IllegalArgumentException("A venda não pode ser nula.");
        }

        if (venda.getIdFuncionario() <= 0) {
            throw new IllegalArgumentException(
                    "A venda deve estar associada a um funcionário válido."
            );
        }

        if (venda.getItensVenda() == null || venda.getItensVenda().isEmpty()) {
            throw new IllegalArgumentException(
                    "A venda deve possuir pelo menos um item."
            );
        }

        if (venda.getValorTotal() < 0) {
            throw new IllegalArgumentException(
                    "O valor total da venda não pode ser negativo."
            );
        }

        if (venda.getValorPago() < 0) {
            throw new IllegalArgumentException(
                    "O valor pago não pode ser negativo."
            );
        }

        if (venda.getValorPago() < venda.getValorTotal()) {
            throw new IllegalArgumentException(
                    "O valor pago não pode ser inferior ao valor total."
            );
        }

        if (venda.getForma() == null) {
            throw new IllegalArgumentException(
                    "A forma de pagamento deve ser informada."
            );
        }
    }
}
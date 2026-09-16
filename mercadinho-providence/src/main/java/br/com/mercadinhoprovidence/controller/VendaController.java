package br.com.mercadinhoprovidence.controller;

import br.com.mercadinhoprovidence.model.Venda;
import br.com.mercadinhoprovidence.service.VendaService;

public class VendaController {

    private final VendaService vendaService;

    public VendaController(VendaService vendaService) {
        this.vendaService = vendaService;
    }

    /**
     * Finaliza e salva a venda.
     *
     * Este método pode ser chamado pela TelaVenda
     * quando o operador confirmar o pagamento.
     *
     * @param venda venda que será finalizada
     * @return ID da venda salva
     */
    public int finalizarVenda(Venda venda) {

        try {
            int idVenda = vendaService.salvarVenda(venda);

            System.out.println(
                    "Venda salva com sucesso. ID: " + idVenda
            );

            return idVenda;

        } catch (IllegalArgumentException e) {

            System.err.println(
                    "Dados inválidos para finalizar venda: "
                    + e.getMessage()
            );

            throw e;

        } catch (RuntimeException e) {

            System.err.println(
                    "Erro ao finalizar venda: "
                    + e.getMessage()
            );

            throw e;
        }
    }
}
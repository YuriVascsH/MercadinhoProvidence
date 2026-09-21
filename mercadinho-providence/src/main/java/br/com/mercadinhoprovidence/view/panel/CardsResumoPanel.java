package br.com.mercadinhoprovidence.view.panel;

import javax.swing.*;

import br.com.mercadinhoprovidence.view.CardInfo;

import java.awt.*;

/**
 * Grid com os cards de indicadores da tela inicial (Vendas do Dia,
 * Itens Críticos, Próx. ao Vencimento).
 *
 * Os valores ainda são fixos; quando os dados vierem do banco, basta
 * trocar o construtor por um que receba os valores prontos, ou adicionar
 * um método atualizarDados(...) que remova e recrie os CardInfo.
 */
public class CardsResumoPanel extends JPanel {

    public CardsResumoPanel() {
        this("R$ 1.250,00", "3 produtos", "8 produtos");
    }

    public CardsResumoPanel(String vendasDoDia, String itensCriticos, String proximosVencimento) {
        super(new GridLayout(1, 3, 15, 0));
        setOpaque(false);

        add(new CardInfo("Vendas do Dia", vendasDoDia, "#2e7d32"));
        add(new CardInfo("Itens Críticos", itensCriticos, "#d32f2f"));
        add(new CardInfo("Próx. ao Vencimento", proximosVencimento, "#ed6c02"));
    }
}

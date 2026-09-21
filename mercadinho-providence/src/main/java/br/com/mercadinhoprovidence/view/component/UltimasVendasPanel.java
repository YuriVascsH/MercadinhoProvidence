package br.com.mercadinhoprovidence.view.component;

import br.com.mercadinhoprovidence.dto.venda.ItemVendaDto;
import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Painel com a tabela de últimas vendas do dia.
 * Extraído de criarPainelUltimasVendas() de TelaInicialView.
 *
 * Quando uma linha é clicada, notifica o listener informado para que quem
 * estiver usando o componente decida como abrir o cupom fiscal (mantendo
 * este painel sem depender diretamente de CupomFiscalDialog).
 */
public class UltimasVendasPanel extends JPanel {

    /** Notificado quando o usuário clica em uma venda da lista. */
    public interface OnVendaSelecionadaListener {
        void aoSelecionarVenda(String codVenda, String total, List<ItemVendaDto> itens);
    }

    private static final String[] COLUNAS = { "Hora", "Cód. Venda", "Total", "Ação" };

    // Dados de exemplo — substituir por consulta ao banco quando disponível.
    private static final Object[][] DADOS_EXEMPLO = {
            { "16:05", "#1042", "R$ 85,90", "📄 Ver Cupom" },
            { "15:48", "#1041", "R$ 12,30", "📄 Ver Cupom" },
            { "15:32", "#1040", "R$ 140,00", "📄 Ver Cupom" },
            { "15:10", "#1039", "R$ 45,50", "📄 Ver Cupom" },
            { "14:55", "#1038", "R$ 210,00", "📄 Ver Cupom" }
    };

    public UltimasVendasPanel(OnVendaSelecionadaListener listener) {
        super(new BorderLayout(0, 12));
        setBackground(Color.WHITE);
        putClientProperty(FlatClientProperties.STYLE, "arc: 12; background: #ffffff");
        setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));

        add(criarTopPanel(), BorderLayout.NORTH);
        add(criarScrollTabela(listener), BorderLayout.CENTER);
    }

    private JPanel criarTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout(0, 10));
        topPanel.setOpaque(false);

        JLabel lblTitulo = new JLabel("🛒  Últimas Vendas do Dia");
        lblTitulo.putClientProperty(FlatClientProperties.STYLE, "font: bold +3; foreground: #222222");

        JSeparator separator = new JSeparator();
        separator.putClientProperty(FlatClientProperties.STYLE, "foreground: #e8e8e8");

        topPanel.add(lblTitulo, BorderLayout.NORTH);
        topPanel.add(separator, BorderLayout.SOUTH);
        return topPanel;
    }

    private JScrollPane criarScrollTabela(OnVendaSelecionadaListener listener) {
        JTable tabela = new JTable(DADOS_EXEMPLO, COLUNAS) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabela.setRowHeight(40);
        tabela.setShowVerticalLines(false);
        tabela.setShowHorizontalLines(true);
        tabela.setGridColor(new Color(235, 235, 235));
        tabela.setFocusable(false);
        tabela.setBackground(Color.WHITE);
        tabela.putClientProperty(FlatClientProperties.STYLE,
                "selectionBackground: #e3f2fd; selectionForeground: #000000; font: 13");

        configurarHeader(tabela);
        configurarRenderers(tabela);
        configurarClique(tabela, listener);

        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(240, 240, 240), 1));
        scrollPane.getViewport().setBackground(Color.WHITE);
        return scrollPane;
    }

    private void configurarHeader(JTable tabela) {
        javax.swing.table.JTableHeader header = tabela.getTableHeader();
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 38));
        header.putClientProperty(FlatClientProperties.STYLE,
                "height: 38; background: #f1f3f5; foreground: #333333; font: bold 13; separatorColor: #ffffff");

        ((javax.swing.table.DefaultTableCellRenderer) header.getDefaultRenderer())
                .setHorizontalAlignment(SwingConstants.CENTER);
    }

    private void configurarRenderers(JTable tabela) {
        tabela.getColumnModel().getColumn(0).setCellRenderer(
                criarRendererCentralizado(Font.BOLD, 13f, new Color(30, 41, 59)));
        tabela.getColumnModel().getColumn(1).setCellRenderer(
                criarRendererCentralizado(Font.BOLD, 13f, new Color(109, 40, 217)));
        tabela.getColumnModel().getColumn(2).setCellRenderer(
                criarRendererCentralizado(Font.BOLD, 14f, new Color(16, 185, 129)));
        tabela.getColumnModel().getColumn(3).setCellRenderer(
                criarRendererCentralizado(Font.BOLD, 12f, new Color(2, 132, 199)));
    }

    private javax.swing.table.DefaultTableCellRenderer criarRendererCentralizado(int estilo, float tamanho,
            Color cor) {
        return new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
                        column);
                lbl.setHorizontalAlignment(SwingConstants.CENTER);
                lbl.setFont(lbl.getFont().deriveFont(estilo, tamanho));
                lbl.setForeground(isSelected ? Color.BLACK : cor);
                return lbl;
            }
        };
    }

    private void configurarClique(JTable tabela, OnVendaSelecionadaListener listener) {
        tabela.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = tabela.getSelectedRow();
                if (row == -1 || listener == null) {
                    return;
                }

                String codVenda = tabela.getValueAt(row, 1).toString();
                String total = tabela.getValueAt(row, 2).toString();

                // Dados de exemplo — substituir pelos itens reais da venda quando vierem do banco.
                List<ItemVendaDto> itensExemplo = List.of(
                        new ItemVendaDto("268", "ARROZ ESTRELINHA TIPO 1 5KG", 2.0, "UN", 22.90),
                        new ItemVendaDto("146", "QUIBE CRU TEMPERADO RECHEADO", 0.223, "KG", 27.90),
                        new ItemVendaDto("138", "FEIJÃO CARIOCA CAMIL 1KG", 1.0, "UN", 8.90));

                listener.aoSelecionarVenda(codVenda, total, itensExemplo);
            }
        });
    }
}
package br.com.mercadinhoprovidence.view.component;

import br.com.mercadinhoprovidence.dto.venda.ItemVendaDto;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Dialog modal que exibe o cupom fiscal de uma venda.
 */
public class CupomFiscalDialog extends JDialog {

    private static final DateTimeFormatter DATA_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter HORA_FMT = DateTimeFormatter.ofPattern("HH:mm");

    public CupomFiscalDialog(Frame owner, String codVenda, String total, String operador,
            List<ItemVendaDto> itens) {
        super(owner, "Cupom Fiscal - " + codVenda, true);

        setResizable(false);
        setSize(420, 660);
        setLocationRelativeTo(owner);

        JPanel content = new JPanel(new BorderLayout(10, 10));
        content.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        content.setBackground(new Color(240, 240, 240));

        double valorTotalNum = Double.parseDouble(total.replace("R$", "").replace(",", ".").trim());
        double valorRecebido = 100.00;
        double troco = valorRecebido - valorTotalNum;

        JEditorPane editorPane = new JEditorPane();
        editorPane.setContentType("text/html");
        editorPane.setEditable(false);
        editorPane.setText(gerarHtmlCupom(codVenda, total, valorRecebido, troco, operador, itens));
        editorPane.setCaretPosition(0);

        JScrollPane scrollPane = new JScrollPane(editorPane);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        content.add(scrollPane, BorderLayout.CENTER);
        add(content);

        configurarAtalhoEsc();
    }

    /**
     * Método para gerar o cupom html para visuaçização
     * 
     * @param codVenda      codigo da venda
     * @param total         valor final da venda
     * @param valorRecebido valor recebido para pagamento
     * @param troco         valor a ser entregue
     * @param operador      nome do funcionário que realizou a venda
     * @param itens         lista de itens a serem acrescentado no cupom
     * @return
     */
    private String gerarHtmlCupom(String codVenda, String total, double valorRecebido, double troco,
            String operador, List<ItemVendaDto> itens) {
        StringBuilder sb = new StringBuilder();

        LocalDateTime agora = LocalDateTime.now();

        sb.append("<html><head><style>")
                .append("  body { font-family: 'Courier New', monospace; background-color: #e5e5e5; padding: 5px; color: #111; }")
                .append("  .receipt { border: 1px solid #ccc; padding: 15px; background: #ffffff; box-shadow: 0px 2px 5px rgba(0,0,0,0.15); }")
                .append("  .header { text-align: center; margin-bottom: 8px; }")
                .append("  .title { font-weight: bold; font-size: 13px; color: #000; }")
                .append("  .sub { font-size: 10px; color: #444; }")
                .append("  .divider { border-top: 1px dashed #777; margin: 8px 0; }")
                .append("  table { width: 100%; font-size: 11px; border-collapse: collapse; }")
                .append("  th { text-align: left; border-bottom: 1px solid #000; padding-bottom: 3px; font-size: 10px; }")
                .append("  td { padding: 2px 0; vertical-align: top; }")
                .append("  .qtd-detalhe { font-size: 10px; color: #555; padding-left: 20px; }")
                .append("  .right { text-align: right; }")
                .append("  .total-row { font-weight: bold; font-size: 13px; }")
                .append("  .highlight-row { font-weight: bold; font-size: 12px; }")
                .append("  .footer { text-align: center; font-size: 10px; margin-top: 12px; font-style: italic; color: #333; }")
                .append("</style></head><body>")
                .append("  <div class='receipt'>")
                .append("    <div class='header'>")
                .append("      <div class='title'>MERCADINHO PROVIDENCE</div>")
                .append("      <div class='sub'>Rua Exemplo, 123 - Centro<br>CNPJ: 00.000.000/0001-00</div>")
                .append("    </div>")
                .append("    <div class='divider'></div>")
                .append("    <div class='sub'>")
                .append("      <b>CUPOM FISCAL:</b> ").append(codVenda).append("<br>")
                .append("      <b>DATA:</b> ").append(agora.format(DATA_FMT))
                .append(" &nbsp;&nbsp;<b>HORA:</b> ").append(agora.format(HORA_FMT)).append("<br>")
                .append("      <b>OPERADOR:</b> ").append(operador.toUpperCase())
                .append("    </div>")
                .append("    <div class='divider'></div>")
                .append("    <table>")
                .append("      <tr><th>ITEM</th><th>CÓD.</th><th>DESC.</th><th class='right'>VALOR</th></tr>");

        if (itens != null && !itens.isEmpty()) {
            for (int i = 0; i < itens.size(); i++) {
                ItemVendaDto item = itens.get(i);
                String idx = String.format("%03d", i + 1);

                sb.append("      <tr>")
                        .append("        <td width='10%'>").append(idx).append("</td>")
                        .append("        <td width='15%'>").append(item.getCodigo()).append("</td>")
                        .append("        <td width='55%'><b>").append(item.getDescricao()).append("</b></td>")
                        .append("        <td width='20%' class='right'>")
                        .append(String.format("R$ %.2f", item.getValorTotal())).append("</td>")
                        .append("      </tr>");

                String detalheQtd;
                if ("KG".equalsIgnoreCase(item.getUnidade()) || "G".equalsIgnoreCase(item.getUnidade())) {
                    detalheQtd = String.format("%.3f %s X R$ %.2f", item.getQuantidade(), item.getUnidade(),
                            item.getValorUnitario());
                } else {
                    detalheQtd = String.format("%.0f %s X R$ %.2f", item.getQuantidade(), item.getUnidade(),
                            item.getValorUnitario());
                }

                sb.append("      <tr>")
                        .append("        <td colspan='4' class='qtd-detalhe'>").append(detalheQtd).append("</td>")
                        .append("      </tr>");
            }
        }

        sb.append("    </table>")
                .append("    <div class='divider'></div>")
                .append("    <table>")
                .append("      <tr class='total-row'><td>TOTAL R$:</td><td class='right'>").append(total)
                .append("</td></tr>")
                .append("      <tr><td>FORMA PAGTO:</td><td class='right'>DINHEIRO</td></tr>")
                .append("      <tr><td>VALOR PAGO R$:</td><td class='right'>")
                .append(String.format("R$ %.2f", valorRecebido)).append("</td></tr>")
                .append("      <tr class='highlight-row'><td>TROCO R$:</td><td class='right'>")
                .append(String.format("R$ %.2f", troco)).append("</td></tr>")
                .append("    </table>")
                .append("    <div class='divider'></div>")
                .append("    <div class='footer'>Obrigado pela preferência!</div>")
                .append("  </div></body></html>");

        return sb.toString();
    }

    private void configurarAtalhoEsc() {
        getRootPane().getInputMap(
                JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                        KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                        "FECHAR_DIALOG");

        getRootPane().getActionMap().put(
                "FECHAR_DIALOG",
                new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dispose();
                    }
                });
    }
}
// package br.com.mercadinhoprovidence.printer;

// import br.com.mercadinhoprovidence.model.Venda;
// import br.com.mercadinhoprovidence.model.relatorio.ProdutosComEstoqueCritico;
// import br.com.mercadinhoprovidence.model.relatorio.ProdutosComValidadeProxima;
// import br.com.mercadinhoprovidence.model.relatorio.ProdutosMaisVendido;
// import br.com.mercadinhoprovidence.model.relatorio.RankDeOperadoresPorVendasTotais;
// import br.com.mercadinhoprovidence.model.relatorio.RelatorioVendasDiarias;
// import javafx.scene.control.TableView;
// import br.com.mercadinhoprovidence.dao.FuncionarioDao;
// import br.com.mercadinhoprovidence.model.ItemVenda;
// import br.com.mercadinhoprovidence.model.Produto;

// import com.fazecast.jSerialComm.SerialPort;

// import java.text.Normalizer;
// import java.io.OutputStream;
// import java.time.format.DateTimeFormatter;
// import java.util.List;

// public class Impressora {

// 	public static void VerificarFuncionamentoDeImpressora() {
// 		StringBuilder sb = new StringBuilder();
// 		sb.append("\u001B\u0040"); // ESC @ (reset)
// 		sb.append("\n\n\n"); // Quebras de linha antes
// 		sb.append("A impressora está ativa\n");
// 		sb.append("\n\n\n"); // Quebras de linha depois
// 		sb.append("\u001D\u0056\u0001"); // Corte de papel

// 		// enviarParaImpressora(sb.toString());
// 	}

// 	public static void imprimirCupom(Venda venda) {
// 		if (venda == null || venda.getItensVenda() == null || venda.getPagamento() == null) {
// 			System.err.println("❌ Venda, itens ou pagamento estão nulos. Não é possível imprimir.");
// 			return;
// 		}

// 		StringBuilder sb = new StringBuilder();
// 		sb.append("\u001B\u0040"); // ESC @ (reset)
// 		sb.append("MERCADINHO PROVIDENCE\n");
// 		sb.append("CUPOM NÃO FISCAL\n");
// 		sb.append("Venda Nº: ").append(venda.getIdVenda()).append("\n");
// 		sb.append("Data: ").append(venda.getDataHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
// 				.append("\n");
// 		sb.append("-----------------------------\n");

// 		for (ItemVenda item : venda.getItensVenda()) {
// 			String nome = item.getNomeProduto();
// 			double qtd = item.getQuantidadeOuPeso();
// 			double unit = item.getPrecoUnitarioVenda();
// 			double total = item.getTotalItem();

// 			sb.append(String.format("%-16s\n", nome));
// 			sb.append(String.format(" %.2f R$ %.2f = R$ %.2f\n", qtd, unit, total));
// 		}

// 		sb.append("-----------------------------\n");
// 		sb.append(String.format("Subtotal:     R$ %.2f\n", venda.getValorSubtotal()));
// 		sb.append(String.format("Desconto:     R$ %.2f\n", venda.getValorDesconto()));
// 		sb.append(String.format("TOTAL:        R$ %.2f\n", venda.getValorTotal()));
// 		sb.append(String.format("Pago:         R$ %.2f\n", venda.getPagamento().getValorPago()));
// 		sb.append(String.format("Troco:        R$ %.2f\n", venda.getPagamento().getTroco()));
// 		sb.append("Forma: ").append(venda.getPagamento().getForma().toString()).append("\n");

// 		sb.append("\nObrigado pela preferência!\n");
// 		sb.append("\u001B\u0064\u0005"); // avança 5 linhas
// 		sb.append("\u001D\u0056\u0001"); // corte

// 		enviarParaImpressora(sb.toString());
// 	}

// 	public static void imprimirCodigoFuncionario(int codigoVerificador) {
// 		System.out.println("Imprimirndo o Codigo de Barras de Identificação do funcionario!");
// 		StringBuilder sb = new StringBuilder();
// 		sb.append("\u001B\u0040"); // Reset
// 		sb.append("Código: ").append(codigoVerificador).append("\n\n");

// 		sb.append("\u001D\u006B\u0004"); // Código de barras CODE39
// 		sb.append(String.valueOf(codigoVerificador));
// 		sb.append("\u0000"); // fim do código
// 		sb.append("\u001B\u0064\u0003"); // avança 3 linhas
// 		sb.append("\u001D\u0056\u0001"); // corte

// 		enviarParaImpressora(sb.toString());
// 	}

// 	public static void imprimirRelatorioVendasDiarias(TableView<RelatorioVendasDiarias> tabela) {
// 		if (tabela == null || tabela.getItems().isEmpty()) {
// 			System.err.println("🚫 Nenhum dado para imprimir.");
// 			return;
// 		}

// 		StringBuilder sb = new StringBuilder();
// 		sb.append("\u001B\u0040"); // ESC @
// 		sb.append("RELATÓRIO DE VENDAS DIÁRIAS\n");
// 		sb.append("-----------------------------\n");

// 		for (RelatorioVendasDiarias r : tabela.getItems()) {
// 			sb.append("Data: ").append(r.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append("\n");
// 			sb.append("Operador: ").append(r.getOperador()).append("\n");
// 			sb.append("Itens: ").append(r.getQuantidadeItens()).append("\n");
// 			sb.append(String.format("Subtotal: R$ %.2f\n", r.getValorSubtotal()));
// 			sb.append(String.format("Desconto: R$ %.2f\n", r.getValorDesconto()));
// 			sb.append(String.format("Total:    R$ %.2f\n", r.getValorTotal()));
// 			sb.append("-----------------------------\n");
// 		}

// 		sb.append("\n\u001B\u0064\u0005"); // avança 5 linhas
// 		sb.append("\u001D\u0056\u0001"); // corte

// 		enviarParaImpressora(sb.toString());
// 	}

// 	public static void imprimirProdutosEstoqueCritico(TableView<ProdutosComEstoqueCritico> produtos) {
// 		if (produtos == null || produtos.getItems().isEmpty()) {
// 			System.err.println("🚫 Nenhum produto crítico para imprimir.");
// 			return;
// 		}

// 		StringBuilder sb = new StringBuilder();
// 		sb.append("\u001B\u0040");
// 		sb.append("PRODUTOS COM ESTOQUE CRÍTICO\n");
// 		sb.append("-----------------------------\n");

// 		for (ProdutosComEstoqueCritico p : produtos.getItems()) {
// 			sb.append("Produto: ").append(p.getNomeProduto()).append("\n");
// 			sb.append("Qtd: ").append(p.getQuantidadeEmEstoque()).append("\n");
// 			sb.append("Validade: ")
// 					.append(p.getValidade() != null
// 							? p.getValidade().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
// 							: "N/A")
// 					.append("\n");
// 			sb.append("Código: ").append(p.getCodigoDeBarras()).append("\n");
// 			sb.append("-----------------------------\n");
// 		}

// 		sb.append("\n\u001B\u0064\u0005");
// 		sb.append("\u001D\u0056\u0001");

// 		enviarParaImpressora(sb.toString());
// 	}

// 	public static void imprimirProdutosValidadeProxima(TableView<ProdutosComValidadeProxima> produtos) {
// 		if (produtos == null || produtos.getItems().isEmpty()) {
// 			System.err.println("🚫 Nenhum produto com validade próxima.");
// 			return;
// 		}

// 		StringBuilder sb = new StringBuilder();
// 		sb.append("\u001B\u0040");
// 		sb.append("VALIDADE PRÓXIMA\n");
// 		sb.append("-----------------------------\n");

// 		for (ProdutosComValidadeProxima p : produtos.getItems()) {
// 			sb.append("Produto: ").append(p.getNomeProduto()).append("\n");
// 			sb.append("Qtd: ").append(p.getQuantidadeEmEstoque()).append("\n");
// 			sb.append("Validade: ").append(p.getValidade().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
// 					.append("\n");
// 			sb.append("-----------------------------\n");
// 		}

// 		sb.append("\n\u001B\u0064\u0005");
// 		sb.append("\u001D\u0056\u0001");

// 		enviarParaImpressora(sb.toString());
// 	}

// 	public static void imprimirProdutosMaisVendidos(TableView<ProdutosMaisVendido> produtos) {
// 		if (produtos == null || produtos.getItems().isEmpty()) {
// 			System.err.println("🚫 Nenhum produto vendido para imprimir.");
// 			return;
// 		}

// 		StringBuilder sb = new StringBuilder();
// 		sb.append("\u001B\u0040");
// 		sb.append("PRODUTOS MAIS VENDIDOS\n");
// 		sb.append("-----------------------------\n");

// 		for (ProdutosMaisVendido p : produtos.getItems()) {
// 			sb.append("Produto: ").append(p.getNomeProduto()).append("\n");
// 			sb.append("Qtd Vendida: ").append(p.getQuantidadeVendida()).append("\n");
// 			sb.append(String.format("Faturamento: R$ %.2f\n", p.getFaturamentoTotal()));
// 			sb.append("Código: ").append(p.getCodigoDeBarras()).append("\n");
// 			sb.append("-----------------------------\n");
// 		}

// 		sb.append("\n\u001B\u0064\u0005");
// 		sb.append("\u001D\u0056\u0001");

// 		enviarParaImpressora(sb.toString());
// 	}

// 	public static void imprimirRankingOperadores(TableView<RankDeOperadoresPorVendasTotais> ranking) {
// 		if (ranking == null || ranking.getItems().isEmpty()) {
// 			System.err.println("🚫 Nenhum operador para imprimir.");
// 			return;
// 		}

// 		StringBuilder sb = new StringBuilder();
// 		sb.append("\u001B\u0040");
// 		sb.append("RANKING DE OPERADORES\n");
// 		sb.append("-----------------------------\n");

// 		for (RankDeOperadoresPorVendasTotais r : ranking.getItems()) {
// 			sb.append("Operador: ").append(r.getNomeOperador()).append("\n");
// 			sb.append("Vendas: ").append(r.getTotalVendas()).append("\n");
// 			sb.append(String.format("Total: R$ %.2f\n", r.getValorTotalVendido()));
// 			sb.append(String.format("Média: R$ %.2f\n", r.getMediaPorVenda()));
// 			sb.append("-----------------------------\n");
// 		}

// 		sb.append("\n\u001B\u0064\u0005");
// 		sb.append("\u001D\u0056\u0001");

// 		enviarParaImpressora(sb.toString());
// 	}

// 	private static void enviarParaImpressora(String texto) {
// 		String porta = "COM4";
// 		SerialPort comPort = SerialPort.getCommPort(porta);
// 		comPort.setComPortParameters(19200, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
// 		comPort.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0);

// 		// Remove acentos e caracteres especiais
// 		String textoLimpo = Normalizer.normalize(texto, Normalizer.Form.NFD)
// 				.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "")
// 				.replaceAll("[^\\p{ASCII}]", "");

// 		if (comPort.openPort()) {
// 			try (OutputStream out = comPort.getOutputStream()) {
// 				// Reset da impressora
// 				out.write(new byte[] { 0x1B, 0x40 }); // ESC @
// 				Thread.sleep(100);

// 				// Seleciona codepage 3 (Portuguese CP860)
// 				out.write(new byte[] { 0x1B, 0x74, 0x03 }); // ESC t 3
// 				out.flush();
// 				Thread.sleep(100);

// 				// Envia texto limpo
// 				byte[] dados = textoLimpo.getBytes("CP860");
// 				out.write(dados);
// 				out.write("\n".getBytes("CP860"));

// 				// Corte de papel
// 				out.write(new byte[] { 0x1D, 0x56, 0x01 }); // GS V 1
// 				out.flush();

// 				System.out.println("✅ Relatório impresso com sucesso!");
// 			} catch (Exception e) {
// 				System.err.println("❌ Erro ao imprimir relatório: " + e.getMessage());
// 			} finally {
// 				comPort.closePort();
// 			}
// 		} else {
// 			System.err.println("🚫 Não foi possível abrir a porta " + porta);
// 		}
// 	}

// 	// Adicione este método à sua classe Impressora
// 	public static void imprimirRelatorioVendas(List<Venda> vendas) {
// 		if (vendas == null || vendas.isEmpty()) {
// 			System.err.println("🚫 Nenhum dado para imprimir.");
// 			return;
// 		}

// 		StringBuilder sb = new StringBuilder();
// 		sb.append("\u001B\u0040"); // ESC @
// 		sb.append("RELATÓRIO DE VENDAS POR PERÍODO\n");
// 		sb.append("-----------------------------\n");

// 		for (Venda v : vendas) {
// 			sb.append("Data: ").append(v.getDataHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append("\n");
// 			// A sua classe Venda não tem o nome do operador diretamente, precisa buscar na
// 			// DAO
// 			FuncionarioDao funcionarioDao = new FuncionarioDao();
// 			String nomeOperador = funcionarioDao.buscarNomePorId(v.getIdFuncionario());
// 			sb.append("Operador: ").append(nomeOperador).append("\n");
// 			// Correção: usando getQuantidadeOuPeso
// 			sb.append("Itens: ").append(v.getItensVenda().stream()
// 					.mapToDouble(ItemVenda::getQuantidadeOuPeso).sum())
// 					.append("\n");
// 			sb.append(String.format("Subtotal: R$ %.2f\n", v.getValorSubtotal()));
// 			sb.append(String.format("Desconto: R$ %.2f\n", v.getValorDesconto()));
// 			sb.append(String.format("Total:    R$ %.2f\n", v.getValorTotal()));
// 			sb.append("-----------------------------\n");
// 		}

// 		sb.append("\n\u001B\u0064\u0005"); // avança 5 linhas
// 		sb.append("\u001D\u0056\u0001"); // corte

// 		enviarParaImpressora(sb.toString());
// 	}

// 	public static void imprimirRelatorioFuncionario(List<Venda> vendasAgrupadas) {
// 		if (vendasAgrupadas == null || vendasAgrupadas.isEmpty()) {
// 			System.err.println("🚫 Nenhum dado para imprimir.");
// 			return;
// 		}

// 		StringBuilder sb = new StringBuilder();
// 		sb.append("\u001B\u0040"); // ESC @
// 		sb.append("RELATÓRIO DE VENDAS POR FUNCIONÁRIO\n");
// 		sb.append("-----------------------------\n");

// 		for (Venda vendaAgrupada : vendasAgrupadas) {
// 			FuncionarioDao funcionarioDao = new FuncionarioDao();
// 			String nomeOperador = funcionarioDao.buscarNomePorId(vendaAgrupada.getIdFuncionario());
// 			sb.append("Operador: ").append(nomeOperador).append("\n");
// 			sb.append(String.format("Total de Vendas: R$ %.2f\n", vendaAgrupada.getValorTotalManual()));
// 			sb.append("-----------------------------\n");
// 		}

// 		sb.append("\n\u001B\u0064\u0005"); // avança 5 linhas
// 		sb.append("\u001D\u0056\u0001"); // corte

// 		enviarParaImpressora(sb.toString());
// 	}

// 	public static void imprimirRelatorioEstoque(List<Produto> produtos) {
// 		if (produtos == null || produtos.isEmpty()) {
// 			System.err.println("🚫 Nenhum produto para imprimir.");
// 			return;
// 		}

// 		StringBuilder sb = new StringBuilder();
// 		sb.append("\u001B\u0040"); // ESC @ (reset)
// 		sb.append("RELATÓRIO DE ESTOQUE\n");
// 		sb.append("-----------------------------\n");
// 		sb.append(String.format("%-15s %-20s %-10s %-15s\n",
// 				"Código", "Produto", "Qtd", "Validade"));
// 		sb.append("-----------------------------\n");

// 		for (Produto p : produtos) {
// 			String codigoBarras = p.getCodigoDeBarras();
// 			String nomeProduto = p.getNome();
// 			double quantidade = p.getQuantidadeOuPesoEmEstoque();
// 			String validade = p.getValidade() != null
// 					? p.getValidade().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
// 					: "N/A";

// 			sb.append(String.format("%-15s %-20s %-10d %-15s\n",
// 					codigoBarras, nomeProduto, quantidade, validade));
// 		}

// 		sb.append("\n\u001D\u0056\u0001");

// 		enviarParaImpressora(sb.toString());
// 	}

// }

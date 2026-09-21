package br.com.mercadinhoprovidence.view.panel;

import br.com.mercadinhoprovidence.dto.login.LoginResponseDto;
import br.com.mercadinhoprovidence.view.component.global.HeaderSection;

import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Objects;

public class TelaFuncionariosPanel extends JPanel {

    private final LoginResponseDto funcionarioLogado;
    private JTable tabelaFuncionarios;
    private DefaultTableModel tableModel;
    private JTextField txtBusca;

    // Cores padronizadas para harmonia com a marca (Laranja #FF5500)
    private static final Color COR_LARANJA_PRIMARY = new Color(255, 85, 0);
    private static final Color COR_FUNDO_CARD = new Color(43, 46, 50);

    // Cores dos botões de ação da tabela
    private static final Color COR_VERDE_EDITAR = new Color(39, 122, 76);
    private static final Color COR_VERDE_EDITAR_HOVER = new Color(46, 145, 91);
    private static final Color COR_VERMELHO_EXCLUIR = new Color(90, 40, 40);
    private static final Color COR_VERMELHO_EXCLUIR_HOVER = new Color(120, 50, 50);

    public TelaFuncionariosPanel(LoginResponseDto funcionarioLogado) {
        this.funcionarioLogado = funcionarioLogado;
        setupUI();
        carregarDadosExemplo();
    }

    private void setupUI() {
        setLayout(new BorderLayout(0, 20));
        setBorder(new EmptyBorder(25, 25, 25, 25));
        setOpaque(false);

        add(new HeaderSection("Funcionários", "Gerencie a equipe, permissões e acessos ao sistema.", "Novo Funcionário",
                  () -> abrirModalFormulario(null)), BorderLayout.NORTH);
        add(criarTabelaCard(), BorderLayout.CENTER);

        // Precisa ser chamado depois que tabelaFuncionarios já existe (criada em
        // criarTabelaCard)
        configurarHoverAcoes();
    }


    private JPanel criarTabelaCard() {
        JPanel cardPanel = new JPanel(new BorderLayout());
        cardPanel.setBackground(COR_FUNDO_CARD);
        cardPanel.putClientProperty(FlatClientProperties.STYLE, "arc: 16; border: 1,1,1,1,#3D4148;");
        cardPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        String[] colunas = { "ID", "Nome", "CPF", "Cargo", "Status", "Ações" };
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == COLUNA_ACOES; // só a coluna de ações é "editável" (clicável)
            }
        };

        tabelaFuncionarios = new JTable(tableModel);
        tabelaFuncionarios.getTableHeader().setResizingAllowed(false);
        estilizarTabela(tabelaFuncionarios);

        JScrollPane scrollPane = new JScrollPane(tabelaFuncionarios);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        cardPanel.add(scrollPane, BorderLayout.CENTER);

        return cardPanel;
    }

    private void estilizarTabela(JTable table) {
        table.setRowHeight(48);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(60, 64, 70));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setResizingAllowed(false);
        table.getTableHeader().putClientProperty(FlatClientProperties.STYLE, "" +
                "font: bold;" +
                "height: 42;" +
                "background: #2B2E32;" +
                "foreground: #A0A0A0;" +
                "separatorColor: #3D4148;");

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        DefaultTableCellRenderer leftPaddingRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBorder(new EmptyBorder(0, 15, 0, 0));
                return this;
            }
        };

        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(0).setPreferredWidth(60);

        table.getColumnModel().getColumn(1).setCellRenderer(leftPaddingRenderer);
        table.getColumnModel().getColumn(1).setPreferredWidth(220);

        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

        table.getColumnModel().getColumn(4).setCellRenderer(new StatusBadgeRenderer());

        table.getColumnModel().getColumn(COLUNA_ACOES).setCellRenderer(new AcoesCellRenderer());
        table.getColumnModel().getColumn(COLUNA_ACOES).setCellEditor(new AcoesCellEditor());
    }

    private void carregarDadosExemplo() {
        tableModel.setRowCount(0);
        tableModel.addRow(new Object[] { "1", "Carlos Silva", "123.456.789-00", "Caixa", "Ativo", "" });
        tableModel.addRow(new Object[] { "2", "Ana Oliveira", "987.654.321-11", "Gerente", "Ativo", "" });
        tableModel.addRow(new Object[] { "3", "Marcos Souza", "456.789.123-22", "Repositor", "Inativo", "" });
    }

    private void abrirModalFormulario(Object funcionarioExistente) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                funcionarioExistente == null ? "Cadastrar Novo Funcionário" : "Editar Funcionário", true);
        dialog.setSize(650, 720);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        // Painel principal com rolagem e margens confortáveis
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        // --- COMPONENTES DO FORMULÁRIO ---
        JTextField txtNome = new JTextField();
        JTextField txtCpf = new JTextField();
        JTextField txtDataNasc = new JTextField(); // Formato AAAA-MM-DD
        JTextField txtEmail = new JTextField();
        JTextField txtTelefone = new JTextField(); // Opcional
        JTextField txtEndereco = new JTextField(); // Opcional

        // Cargo agora é um JComboBox (Enum do Mercado)
        JComboBox<String> cmbCargo = new JComboBox<>(new String[] {
                "Caixa", "Gerente", "Repositor", "Estoquista", "Atendente", "Fiscal de Caixa"
        });

        JTextField txtSalario = new JTextField();
        JPasswordField txtSenha = new JPasswordField();
        JTextField txtDataAdmissao = new JTextField(); // Opcional (Deixe vazio para usar a data atual do BD)
        JCheckBox chkAtivo = new JCheckBox("Funcionário ativo no sistema (Liberado para login)");
        chkAtivo.setSelected(true);

        // Aplicando propriedades visuais do FlatLaf (Arredondamento e Padding)
        JComponent[] textFields = { txtNome, txtCpf, txtDataNasc, txtEmail, txtTelefone, txtEndereco, cmbCargo,
                txtSalario, txtSenha, txtDataAdmissao };
        for (JComponent c : textFields) {
            c.putClientProperty(FlatClientProperties.STYLE, "arc: 10; margin: 6,10,6,10;");
        }

        // Placeholders úteis
        txtCpf.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "000.000.000-00");
        txtDataNasc.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "AAAA-MM-DD");
        txtDataAdmissao.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT,
                "Deixe em branco para usar a data de hoje");

        // --- MONTAGEM DE SEÇÕES ORGANIZADAS ---
        mainPanel.add(criarSecaoPainel("Dados Pessoais", new JComponent[][] {
                { new JLabel("Nome Completo *"), txtNome },
                { new JLabel("CPF *"), txtCpf },
                { new JLabel("Data de Nascimento *"), txtDataNasc }
        }));

        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(criarSecaoPainel("Contato e Endereço", new JComponent[][] {
                { new JLabel("E-mail *"), txtEmail },
                { new JLabel("Telefone"), txtTelefone },
                { new JLabel("Endereço"), txtEndereco }
        }));

        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(criarSecaoPainel("Cargo, Salário e Acesso", new JComponent[][] {
                { new JLabel("Cargo *"), cmbCargo },
                { new JLabel("Salário (R$) *"), txtSalario },
                { new JLabel("Senha de Acesso *"), txtSenha },
                { new JLabel("Data de Admissão"), txtDataAdmissao }
        }));

        // Checkbox de status isolado
        JPanel panelCheck = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 10));
        panelCheck.setOpaque(false);
        panelCheck.add(chkAtivo);
        mainPanel.add(panelCheck);

        // --- PAINEL DE BOTÕES DE AÇÃO ---
        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        panelBotoes.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFocusable(false);
        btnCancelar.addActionListener(e -> dialog.dispose());

        JButton btnSalvar = new JButton(funcionarioExistente == null ? "Salvar Funcionário" : "Salvar Alterações");
        btnSalvar.setBackground(COR_LARANJA_PRIMARY);
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSalvar.putClientProperty(FlatClientProperties.STYLE,
                "arc: 10; borderWidth: 0; margin: 6,15,6,15; font: bold;");

        btnSalvar.addActionListener(e -> {
            // Validação rápida dos obrigatórios do BD
            if (txtNome.getText().trim().isEmpty() || txtCpf.getText().trim().isEmpty() ||
                    txtEmail.getText().trim().isEmpty() || txtSalario.getText().trim().isEmpty() ||
                    txtSenha.getPassword().length == 0) {

                JOptionPane.showMessageDialog(dialog,
                        "Por favor, preencha todos os campos obrigatórios marcados com *.",
                        "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Sucesso / Envio para o Banco de Dados
            JOptionPane.showMessageDialog(dialog, "Funcionário cadastrado com sucesso!");
            dialog.dispose();
        });

        panelBotoes.add(btnCancelar);
        panelBotoes.add(btnSalvar);

        dialog.add(new JScrollPane(mainPanel), BorderLayout.CENTER);
        dialog.add(panelBotoes, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    // Método auxiliar para criar blocos visuais limpos com título
    private JPanel criarSecaoPainel(String titulo, JComponent[][] camposLabels) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(60, 63, 65)),
                titulo, 0, 0, new Font("Segoe UI", Font.BOLD, 12), new Color(180, 180, 180)));
        panel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        for (int i = 0; i < camposLabels.length; i++) {
            gbc.gridy = i;

            // Rótulo
            gbc.gridx = 0;
            gbc.weightx = 0.3;
            panel.add(camposLabels[i][0], gbc);

            // Campo de entrada
            gbc.gridx = 1;
            gbc.weightx = 0.7;
            panel.add(camposLabels[i][1], gbc);
        }

        return panel;
    }

    // --- Renderizador de Status (Badges Coloridos) ---
    private static class StatusBadgeRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
                    column);
            label.setHorizontalAlignment(CENTER);

            String status = String.valueOf(value);
            if ("Ativo".equalsIgnoreCase(status)) {
                label.setText("● Ativo");
                label.setForeground(new Color(46, 204, 113));
            } else {
                label.setText("● Inativo");
                label.setForeground(new Color(231, 76, 60));
            }
            return label;
        }
    }

    // --- Estado de hover para a coluna de ações ---
    private int hoveredRow = -1;
    private String hoveredButton = null; // "editar", "excluir" ou null
    private static final int COLUNA_ACOES = 5; // "ID","Nome","CPF","Cargo","Status","Ações" -> índice 5

    private void configurarHoverAcoes() {
        tabelaFuncionarios.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int row = tabelaFuncionarios.rowAtPoint(e.getPoint());
                int col = tabelaFuncionarios.columnAtPoint(e.getPoint());

                String novoBotao = null;
                if (row != -1 && col == COLUNA_ACOES) {
                    Rectangle cellRect = tabelaFuncionarios.getCellRect(row, col, false);
                    Point pontoRelativo = new Point(e.getX() - cellRect.x, e.getY() - cellRect.y);

                    Component comp = tabelaFuncionarios.getCellRenderer(row, col)
                            .getTableCellRendererComponent(tabelaFuncionarios, null, false, false, row, col);
                    comp.setBounds(cellRect);
                    comp.doLayout();
                    Component alvo = SwingUtilities.getDeepestComponentAt(comp, pontoRelativo.x, pontoRelativo.y);

                    if (alvo instanceof JButton btn) {
                        novoBotao = "Editar".equals(btn.getText()) ? "editar" : "excluir";
                    }
                }

                boolean mudou = row != hoveredRow || !Objects.equals(novoBotao, hoveredButton);
                if (mudou) {
                    int linhaAnterior = hoveredRow;
                    hoveredRow = row;
                    hoveredButton = novoBotao;

                    tabelaFuncionarios.setCursor(novoBotao != null
                            ? new Cursor(Cursor.HAND_CURSOR)
                            : Cursor.getDefaultCursor());

                    if (linhaAnterior != -1)
                        repintarLinha(linhaAnterior);
                    if (row != -1)
                        repintarLinha(row);
                }
            }
        });

        tabelaFuncionarios.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                if (hoveredRow != -1) {
                    int linha = hoveredRow;
                    hoveredRow = -1;
                    hoveredButton = null;
                    tabelaFuncionarios.setCursor(Cursor.getDefaultCursor());
                    repintarLinha(linha);
                }
            }
        });
    }

    private void repintarLinha(int row) {
        Rectangle rect = tabelaFuncionarios.getCellRect(row, COLUNA_ACOES, false);
        tabelaFuncionarios.repaint(rect);
    }

    // --- Renderizador dos Botões de Ação ---
    private class AcoesCellRenderer extends PainelAcoes implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
            boolean hoverEditar = row == hoveredRow && "editar".equals(hoveredButton);
            boolean hoverExcluir = row == hoveredRow && "excluir".equals(hoveredButton);

            btnEditar.setBackground(hoverEditar ? COR_VERDE_EDITAR_HOVER : COR_VERDE_EDITAR);
            btnExcluir.setBackground(hoverExcluir ? COR_VERMELHO_EXCLUIR_HOVER : COR_VERMELHO_EXCLUIR);

            return this;
        }
    }

    // --- Editor das Células para Interatividade (Clique na Tabela) ---
    private class AcoesCellEditor extends AbstractCellEditor implements TableCellEditor {
        private final PainelAcoes painel;

        public AcoesCellEditor() {
            this.painel = new PainelAcoes();
            painel.btnEditar.addActionListener(e -> {
                fireEditingStopped();
                int row = tabelaFuncionarios.getSelectedRow();
                if (row != -1) {
                    Object id = tabelaFuncionarios.getValueAt(row, 0);
                    abrirModalFormulario(id);
                }
            });
            painel.btnExcluir.addActionListener(e -> {
                fireEditingStopped();
                int row = tabelaFuncionarios.getSelectedRow();
                if (row != -1) {
                    String id = String.valueOf(tabelaFuncionarios.getValueAt(row, 0));
                    String nome = String.valueOf(tabelaFuncionarios.getValueAt(row, 1));
                    confirmarExclusao(id, nome, () -> System.out.println("olá"));
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
                int column) {
            return painel;
        }

        @Override
        public Object getCellEditorValue() {
            return "";
        }
    }

    // --- Painel Base Reutilizável para os Botões da Tabela ---
    private static class PainelAcoes extends JPanel {
        final JButton btnEditar = new JButton("Editar");
        final JButton btnExcluir = new JButton("Excluir");

        public PainelAcoes() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 6, 8));
            setOpaque(false);
            configurarBotao(btnEditar, COR_VERDE_EDITAR, COR_VERDE_EDITAR_HOVER, "#FFFFFF");
            configurarBotao(btnExcluir, COR_VERMELHO_EXCLUIR, COR_VERMELHO_EXCLUIR_HOVER, "#FF6B6B");
            add(btnEditar);
            add(btnExcluir);
        }

        private void configurarBotao(JButton btn, Color bg, Color bgHover, String fgHex) {
            btn.setFocusable(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.setBackground(bg);
            btn.putClientProperty(FlatClientProperties.STYLE,
                    "arc: 8; foreground: " + fgHex + "; borderWidth: 0; margin: 2,8,2,8;");
            btn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    btn.setBackground(bgHover);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    btn.setBackground(bg);
                }
            });
        }
    }

    private void confirmarExclusao(String id, String nome, Runnable aoConfirmar) {
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Confirmar exclusão",
                Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setUndecorated(true);
        dialog.setLayout(new BorderLayout());

        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBackground(COR_FUNDO_CARD);
        painel.putClientProperty(FlatClientProperties.STYLE, "arc: 16; border: 1,1,1,1,#3D4148;");
        painel.setBorder(new EmptyBorder(24, 24, 20, 24));

        JLabel lblTitulo = new JLabel("Excluir funcionário");
        lblTitulo.putClientProperty(FlatClientProperties.STYLE, "font: +2 bold; foreground: #FFFFFF;");
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblMensagem = new JLabel("<html>Deseja realmente excluir <b>" + nome
                + "</b>? Essa ação não pode ser desfeita.</html>");
        lblMensagem.putClientProperty(FlatClientProperties.STYLE, "foreground: #B0B0B0;");
        lblMensagem.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblMensagem.setBorder(new EmptyBorder(8, 0, 20, 0));

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        botoes.setOpaque(false);
        botoes.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFocusable(false);
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.putClientProperty(FlatClientProperties.STYLE,
                "arc: 10; borderWidth: 1; borderColor: #3D4148; background: #2B2E32; foreground: #FFFFFF;");
        btnCancelar.addActionListener(e -> dialog.dispose());

        JButton btnExcluir = new JButton("Excluir");
        btnExcluir.setFocusable(false);
        btnExcluir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnExcluir.setForeground(Color.WHITE);
        btnExcluir.setBackground(new Color(231, 76, 60));
        btnExcluir.putClientProperty(FlatClientProperties.STYLE, "arc: 10; borderWidth: 0;");
        btnExcluir.addActionListener(e -> {
            dialog.dispose();
            aoConfirmar.run();
        });

        botoes.add(btnCancelar);
        botoes.add(btnExcluir);

        painel.add(lblTitulo);
        painel.add(lblMensagem);
        painel.add(botoes);

        dialog.add(painel, BorderLayout.CENTER);
        dialog.pack();
        dialog.setSize(360, dialog.getPreferredSize().height);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
}
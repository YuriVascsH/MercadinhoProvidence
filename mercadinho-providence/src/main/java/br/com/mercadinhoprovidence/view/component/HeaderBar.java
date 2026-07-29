package br.com.mercadinhoprovidence.view.component;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.formdev.flatlaf.FlatClientProperties;

import br.com.mercadinhoprovidence.dto.login.LoginResponseDto;
import br.com.mercadinhoprovidence.util.TimeUtils;

public class HeaderBar extends JPanel {

    private final LoginResponseDto funcionarioLogado;
    private JLabel user;
    private JLabel hour;

    public HeaderBar(LoginResponseDto funcionarioLogado) {
        this.funcionarioLogado = funcionarioLogado;
        setupLayout();
        buildHeader();
    }

    /**
     * Método para aplicar os estilos e definir o layout
     */
    public void setupLayout() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        putClientProperty(FlatClientProperties.STYLE,
                "background: #ff2200; " +
                        "foreground: #ffffff");

    }

    /**
     * Método para aplicar a lógica do header
     */
    private void buildHeader() {
        // --- Lado Esquerdo: Título da Aplicação ---
        JLabel titleLabel = new JLabel("Mercadinho Providence");
        titleLabel.putClientProperty(FlatClientProperties.STYLE, "font: bold +4; foreground: #ffffff");
        add(titleLabel, BorderLayout.WEST);

        // --- Lado Direito: Informações do Usuário e Relógio ---
        JPanel rightBox = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightBox.setOpaque(false);

        String nomeUsuario = (funcionarioLogado != null && funcionarioLogado.getName() != null)
                ? funcionarioLogado.getName().toUpperCase()
                : "USUÁRIO";

        user = new JLabel("Funcionário: " + nomeUsuario + "   |   ");
        user.putClientProperty(FlatClientProperties.STYLE, "font: bold; foreground: #ffffff");

        hour = new JLabel();
        hour.putClientProperty(FlatClientProperties.STYLE, "foreground: #ffffff");

        TimeUtils.startClock(hour);

        rightBox.add(user);
        rightBox.add(hour);

        add(rightBox, BorderLayout.EAST);
    }

}

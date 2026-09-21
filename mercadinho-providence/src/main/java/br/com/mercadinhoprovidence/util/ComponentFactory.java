package br.com.mercadinhoprovidence.util;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.ParseException;

import javax.swing.JFormattedTextField;
import javax.swing.text.MaskFormatter;

import com.formdev.flatlaf.FlatClientProperties;

import br.com.caelum.stella.validation.CPFValidator;

public class ComponentFactory {

    private ComponentFactory() {
    }

    /**
     * 
     * @return
     */
    public static JFormattedTextField createTextFieldCpf() {
        JFormattedTextField txtCpf;
        try {
            MaskFormatter mask = new MaskFormatter("###.###.###-##");
            mask.setPlaceholderCharacter('_');
            mask.setValueContainsLiteralCharacters(false);
            txtCpf = new JFormattedTextField(mask);
        } catch (ParseException e) {
            txtCpf = new JFormattedTextField();
        }
        txtCpf.putClientProperty(FlatClientProperties.STYLE, "arc: 10; margin: 6,10,6,10;");

        // Evento defensivo: Valida quando o campo perde o foco
        txtCpf.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                validateCpfVisual((JFormattedTextField) e.getSource());
            }
        });
        return txtCpf;
    }

    /**
     * 
     * @param txtCpf
     * @return
     */
    public static boolean validateCpfVisual(JFormattedTextField txtCpf) {
        String valor = txtCpf.getText();
        String apenasNumeros = valor.replaceAll("\\D", "");

        // Se o campo estiver em branco, remove aviso visual
        if (apenasNumeros.isEmpty()) {
            txtCpf.putClientProperty(FlatClientProperties.OUTLINE, null);
            return false;
        }

        boolean eValido = isCpfValidoStella(valor);

        if (eValido) {
            // Remove o contorno de erro (volta à cor padrão)
            txtCpf.putClientProperty(FlatClientProperties.OUTLINE, null);
            return true;
        } else {
            // FlatLaf destaca a borda em vermelho
            txtCpf.putClientProperty(FlatClientProperties.OUTLINE, FlatClientProperties.OUTLINE_ERROR);
            return false;
        }
    }

    /**
     * 
     * @param cpf
     * @return
     */
    private static boolean isCpfValidoStella(String cpf) {
        try {
            CPFValidator validator = new CPFValidator();
            validator.assertValid(cpf);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}

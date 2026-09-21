package br.com.mercadinhoprovidence.view.component.funcionario;

import java.awt.Frame;
import java.awt.Window;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.formdev.flatlaf.FlatClientProperties;

import br.com.mercadinhoprovidence.util.ComponentFactory;

public class EmployeeFormDialog extends JDialog {

    private JTextField txtName;
    private JTextField txtCpf;
    private JTextField txtDate;
    private JTextField txtEmail;
    private JTextField txtPhone;
    private JTextField txtAddress;
    private JComboBox<String> cmbCargo;
    private JTextField txtSalary;
    private JPasswordField txtPassword;
    private JTextField txtDateAdmissao;
    private JCheckBox chkActive;

    private final boolean isEdition;

    private final Object employeeExists;

    public EmployeeFormDialog(Window parent, Object employeeExists) {
        super(tratarParent(parent), employeeExists == null ? "Cadastro de funcionário" : "Editar Funcionário",
                ModalityType.APPLICATION_MODAL);
        this.employeeExists = employeeExists;

        initializerComponents();
    }

    private void initializerComponents() {
        txtName = new JTextField();
        txtCpf = new JTextField();
        txtDate = new JTextField();
        txtEmail = new JTextField();
        txtPhone = new JTextField();
        txtAddress = new JTextField();
        // Alterar futuramente
        cmbCargo = new JComboBox<String>(new String[] { "Funcionário, Líder" });
        txtSalary = new JTextField();
        txtPassword = new JPasswordField();
        txtDateAdmissao = new JTextField();
        chkActive = new JCheckBox("Funcionário ativo no sistema (Liberado para login)");
        chkActive.setSelected(true);

        JComponent[] textFields = { txtName, txtDate, txtEmail, txtPhone, txtAddress, cmbCargo, txtSalary,
                txtPassword, txtDateAdmissao };
        // Rever isso aqui
        txtCpf = ComponentFactory.createTextFieldCpf();
        
        for (JComponent c : textFields) {
            c.putClientProperty(FlatClientProperties.STYLE, "arc: 10; margin: 6,10,6,10;");
        }

    }

    /**
     * Garante que o diálogo nunca receba uma janela pai inválida.
     */
    private static Window tratarParent(Window parent) {
        if (parent != null) {
            return parent;
        }
        // Se parent for null, tenta pegar a janela principal focada no momento
        for (Frame frame : Frame.getFrames()) {
            if (frame.isActive() || frame.isVisible()) {
                return frame;
            }
        }
        return null;
    }
}

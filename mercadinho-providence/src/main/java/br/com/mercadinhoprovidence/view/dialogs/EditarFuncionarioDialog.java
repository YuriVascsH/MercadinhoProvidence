package br.com.mercadinhoprovidence.view.dialogs;

import br.com.mercadinhoprovidence.model.Funcionario;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import br.com.mercadinhoprovidence.controller.FuncionarioController;
import br.com.mercadinhoprovidence.dto.funcionario.FuncionarioResponseDto;
import br.com.mercadinhoprovidence.dto.funcionario.FuncionarioUpdateDto;
import br.com.mercadinhoprovidence.model.enums.Cargo;
//import br.com.mercadinhoprovidence.printer.Impressora;
import br.com.mercadinhoprovidence.util.AlertUtils;
import br.com.mercadinhoprovidence.util.InputUtils;

public class EditarFuncionarioDialog {

    public static void show(Stage ownerStage, Integer id, Runnable onCompletion) {
        FuncionarioController funcionarioController = new FuncionarioController();

        Funcionario funcionario = funcionarioController.buscarPeloId(id);

        FuncionarioUpdateDto dadosParaAtualizar = new FuncionarioUpdateDto();

        Map<String, Node> campos = new HashMap<String, Node>();
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Editar Funcionário");
        dialogStage.setResizable(false);

        dialogStage.initOwner(ownerStage);
        dialogStage.initModality(Modality.WINDOW_MODAL);


        VBox mainLayout = new VBox(20);
        mainLayout.setPadding(new Insets(25));
        mainLayout.getStyleClass().add("main-layout");

        Label titleLabel = new Label("Editar Funcionário: " + funcionario.getNome());
        titleLabel.getStyleClass().add("edit-title");
        titleLabel.setMaxWidth(Double.MAX_VALUE);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(15);
        gridPane.setVgap(12);
        gridPane.setPadding(new Insets(0, 0, 10, 0));
        gridPane.setAlignment(Pos.CENTER);

        String editableStyle = "-fx-border-color: #adb5bd; -fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 8px; -fx-font-size: 14px;";
        String labelStyle = "-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #495057;";

        // CAMPOS APENAS PARA LEITURA (Identificadores e datas fixas)
        TextField tfId = customTextField(String.valueOf(funcionario.getIdFuncionario()), false, "field-readonly", 250, null);

        TextField tfCpf = customTextField(funcionario.getCpf(), false, "field-readonly", 250, null);

        TextField tfDataAdmissao = customTextField(funcionario.getDataAdmissao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), false, "field-readonly", 250, null);

        // CAMPOS EDITÁVEIS
        TextField tfNome = customTextField(funcionario.getNome(), true,"field-editable", 250, "Nome Completo");

        TextField tfTelefone = customTextField(funcionario.getTelefone(), true, "field-editable", 250, "(99) 99999-9999");

        TextField tfEmail = customTextField(funcionario.getEmail(), true, "field-editable", 250, "exemplo@email.com");

        TextField tfSenha = customTextField(funcionario.getEmail(), false, "field-readonly", 250, null);

        TextField tfEndereco = customTextField(funcionario.getEndereco(), true, "field-editable", 250, "Rua Fulana Maria");

        DatePicker dpDataNascimento = new DatePicker();
        dpDataNascimento.setPrefWidth(250);
        dpDataNascimento.setPromptText("dd/MM/yyyy");
        dpDataNascimento.setConverter(InputUtils.getDateConverter());
        if (funcionario.getDataNascimento() != null) {
            dpDataNascimento.setValue(funcionario.getDataNascimento());
        }
        dpDataNascimento.setStyle(editableStyle);

        ComboBox<Cargo> cbCargo = new ComboBox<>();
        cbCargo.getItems().addAll(Cargo.values());
        cbCargo.setValue(funcionario.getCargo());
        cbCargo.setStyle(editableStyle);
        cbCargo.setPrefWidth(250);

        TextField tfSalario = customTextField(String.format(Locale.US, "%.2f", funcionario.getSalario()).replace('.', ','), true,"field-editable",250, "Salário(R$ 2.500,00)");
        InputUtils.setupNumericField(tfSalario, true, 20);

        CheckBox cbAtivo = new CheckBox("Funcionário Ativo");
        cbAtivo.setSelected(funcionario.getAtivo());
        cbAtivo.setStyle(labelStyle);

        TextField tfCodigoBarras = customTextField(String.valueOf(funcionario.getCodigoVerificador()), false, "read-only", 180, null);

        Button btnImprimirCodigo = new Button("Imprimir Código");
        btnImprimirCodigo.getStyleClass().add("btn-print-code");
        btnImprimirCodigo.setOnMouseEntered(e -> btnImprimirCodigo.getStyleClass().add("btn-print-code-hover"));
        btnImprimirCodigo.setOnMouseExited(e -> btnImprimirCodigo.getStyleClass().remove("btn-print-code-hover"));

        // Ação do botão
        btnImprimirCodigo.setOnAction(e -> {
            try {
                //Impressora.imprimirCodigoFuncionario(funcionario.getCodigoVerificador());
            } catch (Exception ex) {
                AlertUtils.showError("Erro ao Imprimir", "Falha na Impressão",
                        "Não foi possível gerar a impressão do código de barras. Detalhes: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        HBox codigoBarrasBox = new HBox(10, tfCodigoBarras, btnImprimirCodigo);
        codigoBarrasBox.setAlignment(Pos.CENTER_LEFT);

        campos.put("ID", tfId);
        campos.put("CPF: ", tfCpf);
        campos.put("Nome: ", tfNome);
        campos.put("Data de Nascimento", dpDataNascimento);
        campos.put("Telefone", tfTelefone);
        campos.put("E-mail", tfEmail);
        campos.put("Senha: ", tfSenha);
        campos.put("Endereço", tfEndereco);
        campos.put("Cargo: ", cbCargo);
        campos.put("Salário: ", tfSalario);
        campos.put("Data de admissão: ", tfDataAdmissao);
        campos.put("Código de barras: ", codigoBarrasBox);

        int row = 0;
        for (Map.Entry<String, Node> entry : campos.entrySet()) {
            Label label = new Label(entry.getKey());
            label.setStyle(labelStyle);
            Node campo = entry.getValue();

            gridPane.add(label, 0, row);
            gridPane.add(campo, 1, row);
            row++;
        }
        // --- Botões de Ação ---
        Button btnSalvar = new Button("Salvar Alterações");
        btnSalvar.getStyleClass().add("btn-save");

        Button btnCancelar = new Button("Cancelar");
        btnCancelar.getStyleClass().add("btn-cancel");

        btnSalvar.setOnAction(e -> {
            // 1. Validação de campos OBRIGATÓRIOS (Nome, Telefone, Cargo)
            if (tfNome.getText().trim().isEmpty() ||
                    tfTelefone.getText().trim().isEmpty() ||
                    cbCargo.getValue() == null) {

                AlertUtils.showError("Campos Obrigatórios", "Erro de Preenchimento",
                        "Por favor, preencha os campos obrigatórios: Nome, Telefone, Salário e Cargo.");
                return;
            }

            // 2. Validação Condicional de EMAIL (se preenchido, deve ser válido)
            String emailText = tfEmail.getText().trim();
            if (!emailText.isEmpty() && !InputUtils.validateEmail(emailText)) {
                AlertUtils.showError("Formato Inválido", "Email",
                        "O Email está em um formato inválido. Deixe o campo vazio se não for informar.");
                return;
            }

            try {
                BigDecimal salario;
                try {
                    salario = InputUtils.parseBigDecimalFromCommaString(tfSalario.getText());
                } catch (NumberFormatException nfe) {
                    AlertUtils.showError("Formato Inválido", "Salário",
                            "O valor do Salário está em um formato inválido. Use vírgula para decimais.");
                    return;
                }

                // 4. TRATAMENTO DOS CAMPOS NÃO OBRIGATÓRIOS PARA NULL/STRING VAZIA
                String email = emailText;

                String endereco = tfEndereco.getText().trim();

                LocalDate dataNascimento = dpDataNascimento.getValue() != null
                        ? LocalDate.from(dpDataNascimento.getValue())
                        : null;

                // 5. Atualizar o objeto funcionario com os novos valores
                // Isso aqui deve ser atualizado para ser um funcionarioUpdateDto Isso aqui vai virar a função de verificação

                dadosParaAtualizar.setNome(tfNome.getText().trim());
                dadosParaAtualizar.setTelefone(tfTelefone.getText().trim());
                dadosParaAtualizar.setEmail(email);
                dadosParaAtualizar.setEndereco(endereco);
                dadosParaAtualizar.setSalario(salario);
                dadosParaAtualizar.setCargo(cbCargo.getValue());
                dadosParaAtualizar.setAtivo(cbAtivo.isSelected());

                // Passar o id + os dados de atualização e retornar um optional
                Optional<FuncionarioResponseDto> atualizadoComSucesso = funcionarioController.atualizar(funcionario.getIdFuncionario(),dadosParaAtualizar);

                if (atualizadoComSucesso.isPresent()) {
                    AlertUtils.showSuccess("Sucesso", "Funcionário Atualizado",
                            "Funcionário '" + atualizadoComSucesso.get().getNome() + "' atualizado com sucesso!");
                    if (onCompletion != null) {
                        onCompletion.run();
                    }
                    dialogStage.close();
                }
            } catch (Exception ex) {
                AlertUtils.showError("Erro Inesperado", "Ocorreu um erro",
                        "Ocorreu um erro inesperado ao tentar salvar as alterações do funcionário. Detalhes: "
                                + ex.getMessage());
                System.err.println("Erro inesperado ao salvar funcionário: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        btnCancelar.setOnAction(e -> dialogStage.close());

        HBox buttonBox = new HBox(15, btnSalvar, btnCancelar);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));

        gridPane.add(buttonBox, 0, row, 2, 1);

        mainLayout.getChildren().addAll(titleLabel, gridPane);

        Scene scene = new Scene(mainLayout);
        dialogStage.setScene(scene);
        dialogStage.showAndWait();
    }

    /**
     * Metodo auxiliar para a criação do campo de textfield
     *
     * @param value Indica o que será inserido para preencher no campo.
     * @param editable Valor Booleano que define se o campo será ativo ou não.
     * @param pathStyle Indica o nome do componente que será utilizado para receber o estilo css.
     * @param width Tamanho da largura do campo.
     * @param prompt Texto para auxiliar o usaário.
     *
     * @return retorna o objeto textField criado e com seus respectivos atributos.
     */
    private static TextField customTextField(String value, Boolean editable, String pathStyle, Integer width, String prompt) {
        TextField textField = new TextField(value);
        textField.setEditable(editable);
        textField.getStyleClass().add(pathStyle);
        if (width == null) {
            textField.setPrefWidth(250);
        }
        textField.setPrefWidth(width);
        if (prompt != null) {
            textField.setPromptText(prompt);
        }
        return textField;
    }

}
package br.com.mercadinhoprovidence.util;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputUtils {

    // Formatter para datas no formato dd/MM/yyyy
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Aplica um TextFormatter a um TextField para limitar o número de caracteres e permitir apenas dígitos.
     * @param textField O TextField a ser limitado.
     * @param maxLength O número máximo de caracteres permitidos.
     */
    public static void limitDigitsNumber(TextField textField, int maxLength){
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*") && newText.length() <= maxLength) {
                return change;
            }
            return null;
        };
        textField.setTextFormatter(new TextFormatter<>(filter));
    }

    /**
     * Valida se a senha tem o comprimento mínimo.
     *
     * @param minLength Tamnanho mínimo da senha.
     * @param password A senha a ser validada.
     * @return true se a senha é válida, false caso contrário.
     */
    public static boolean validatePassword(String password, int minLength) {
        return password != null && password.length() >= minLength;
    }

    /**
     * Valida o formato de um email usando uma expressão regular simples.
     *
     * @param email O email a ser validado.
     * @return true se o email tem um formato válido, false caso contrário.
     */
    public static boolean validateEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * Valida um número de telefone. Permite 10 ou 11 dígitos numéricos.
     *
     * @param telefone O número de telefone a ser validado.
     * @return true se o telefone é válido, false caso contrário.
     */
    public static boolean validatePhone(String telefone) {
        telefone = telefone.replaceAll("[^0-9]", "");
        return telefone.matches("^\\d{10,11}$");
    }

    /**
     * Aplica um TextFormatter a um TextField para limitar o número de caracteres e permitir apenas dígitos.
     *
     * @param textField O TextField ou PasswordField a ser limitado.
     * @param maxLength O número máximo de caracteres permitidos.
     */
    public static void limitDigits(TextField textField, int maxLength) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*") && newText.length() <= maxLength) {
                return change;
            }
            return null;
        };
        textField.setTextFormatter(new TextFormatter<>(filter));
    }

    /**
     * Validação mais robusta de CPF.
     *
     * @param cpf O CPF a ser validado.
     * @return true se o CPF é válido, false caso contrário.
     */
    public static boolean validateCPF(String cpf) {
        // Remove caracteres não numéricos
        cpf = cpf.replaceAll("[^0-9]", "");

        // Verifica se tem 11 dígitos e se não são todos iguais
        if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        // Calcula o primeiro dígito verificador
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += (cpf.charAt(i) - '0') * (10 - i);
        }
        int remainder = 11 - (sum % 11);
        int digit1 = (remainder == 10 || remainder == 11) ? 0 : remainder;

        // Verifica o primeiro dígito
        if (digit1 != (cpf.charAt(9) - '0')) {
            return false;
        }

        // Calcula o segundo dígito verificador
        sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += (cpf.charAt(i) - '0') * (11 - i);
        }
        remainder = 11 - (sum % 11);
        int digit2 = (remainder == 10 || remainder == 11) ? 0 : remainder;

        // Verifica o segundo dígito
        return digit2 == (cpf.charAt(10) - '0');
    }

    /**
     * Aplica um TextFormatter a um TextField para limitar o número de caracteres.
     *
     * @param textField O TextField ou PasswordField a ser limitado.
     * @param maxLength O número máximo de caracteres permitidos.
     */
    public static void limitCharacters(TextField textField, int maxLength) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.length() > maxLength) {
                return null;
            }
            return change;
        };
        textField.setTextFormatter(new TextFormatter<>(filter));
    }

    // --- Métodos para DatePicker e Números Decimais ---

    /**
     * Retorna um StringConverter para LocalDate no formato dd/MM/yyyy.
     * Este converter agora tenta parsear também o formato ddmmyyyy (8 dígitos).
     * Útil para DatePicker.
     */
    public static StringConverter<LocalDate> getDateConverter() {
        return new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                // Converte LocalDate para String no formato dd/MM/yyyy
                return (date != null) ? DATE_FORMATTER.format(date) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    String cleanedString = string.trim().replaceAll("[^0-9]", ""); // Remove tudo que não for dígito

                    // Tenta parsear no formato dd/MM/yyyy primeiro
                    try {
                        return LocalDate.parse(string, DATE_FORMATTER);
                    } catch (DateTimeParseException e1) {
                        // Se falhar, tenta parsear como ddmmyyyy (8 dígitos)
                        if (cleanedString.length() == 8) {
                            try {
                                // Cria um formato temporário para ddmmyyyy
                                DateTimeFormatter tempFormatter = DateTimeFormatter.ofPattern("ddMMyyyy");
                                return LocalDate.parse(cleanedString, tempFormatter);
                            } catch (DateTimeParseException e2) {
                                System.err.println("Erro ao converter data '" + string + "' (formato ddmmyyyy): " + e2.getMessage());
                                // Retorna null se falhar em ambos os formatos
                                return null;
                            }
                        }
                        System.err.println("Erro ao converter data '" + string + "' (formato dd/MM/yyyy): " + e1.getMessage());
                        return null; // Retorna nulo para indicar falha na conversão
                    }
                }
                return null;
            }
        };
    }

    /**
     * Configura um TextField para aceitar apenas valores numéricos (inteiros ou decimais com vírgula).
     *
     * @param textField O TextField a ser configurado.
     * @param allowDecimals Se true, permite decimais com vírgula.
     * @param maxLength O número máximo de caracteres permitidos.
     */
    public static void setupNumericField(TextField textField, boolean allowDecimals, int maxLength) {
        // Regex para validar a entrada numérica enquanto o usuário digita
        // Permite números, e opcionalmente uma vírgula seguida de mais números se allowDecimals for true
        Pattern validPattern;
        if (allowDecimals) {
            // Permite dígitos, opcionalmente uma vírgula e dígitos após.
            // Permite que o campo comece vazio ou com apenas "0," ou ","
            validPattern = Pattern.compile("^\\d*([,]\\d*)?$");
        } else {
            // Apenas dígitos
            validPattern = Pattern.compile("^\\d*$");
        }

        // TextFormatter para aplicar a validação e o limite de caracteres
        textField.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();

            // 1. Limita o número total de caracteres
            if (newText.length() > maxLength) {
                return null; // Rejeita a mudança se exceder o comprimento máximo
            }

            // 2. Valida o formato numérico
            if (newText.isEmpty() || validPattern.matcher(newText).matches()) {
                // Impede múltiplos zeros à esquerda a menos que seja "0," ou "0"
                if (!newText.equals("0") && !newText.equals("0,") && newText.startsWith("0") && newText.length() > 1 && !newText.contains(",")) {
                    // Remove o zero inicial extra se não for 0. algo
                    change.setText(newText.substring(1));
                    change.setCaretPosition(change.getControlCaretPosition() -1 ); // Ajusta o cursor
                    change.setAnchor(change.getControlAnchor() - 1); // Ajusta o âncora
                    return change;
                }
                return change; // Aceita a mudança
            }
            return null; // Rejeita a mudança se não corresponder ao padrão numérico
        }));
    }

    /**
     * Converte uma String com vírgula para Double, utilizando o Locale brasileiro.
     * @param value A String a ser convertida (ex: "123,45").
     * @return O valor Double.
     * @throws NumberFormatException se a String não for um número válido.
     */
    public static Double parseDoubleFromCommaString(String value) throws NumberFormatException {
        if (value == null || value.trim().isEmpty()) {
            return 0.0;
        }
        // Substitui a vírgula por ponto para permitir parsear com Double.parseDouble
        return Double.parseDouble(value.trim().replace(',', '.'));
    }

    /**
     * Converte um Double para String no formato brasileiro (vírgula como decimal),
     * com duas casas decimais.
     * @param value O valor Double.
     * @return A String formatada (ex: "123,45").
     */
    public static String formatDoubleToCommaString(Double value) {
        if (value == null) {
            return "";
        }
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("pt", "BR"));
        DecimalFormat format = new DecimalFormat("#,##0.00", symbols); // Duas casas decimais fixas
        return format.format(value);
    }

    /**
     * Valida se uma string é um número válido (inteiro ou decimal).
     * @param text A string a ser validada.
     * @param allowDecimals Se true, permite números decimais.
     * @return True se a string é um número válido, false caso contrário.
     */
    public static boolean isValidNumericInput(String text, boolean allowDecimals) {
        if (text == null || text.trim().isEmpty()) {
            return false;
        }
        String cleanedText = text.trim().replace(',', '.'); // Normaliza para ponto decimal
        try {
            if (allowDecimals) {
                Double.parseDouble(cleanedText);
            } else {
                Integer.parseInt(cleanedText);
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
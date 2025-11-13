package br.com.mercadinhoprovidence.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class FormatUtils {

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.of("pt", "BR"));

    /**
     * Formata um booleano para "Sim" ou "Não".
     *
     * @param value Valor booleano.
     * @return "Sim" se true, "Não" se false.
     */
    public static String formatBoolean(Boolean value) {
        if (value == null) {
            return "Não informado";
        }
        return value ? "Sim" : "Não";
    }

    /**
     * Formata um LocalDate para String no formato "dd/MM/yyyy".
     *
     * @param date Data a ser formatada.
     * @return String formatada da data.
     */
    public static String formatDate(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.format(dateFormatter);
    }

    /**
     * Formata um valor BigDecimal como moeda brasileira (R$).
     *
     * @param unformattedSalary Salário sem formatação.
     * @return Retorna o salário formatado como moeda.
     */
    public static String formatCurrency(BigDecimal unformattedSalary) {
        if (unformattedSalary == null) {
            return currencyFormat.format(BigDecimal.ZERO);
        }
        return currencyFormat.format(unformattedSalary);
    }

    /**
     * @param cpfSemFormato cpf que vem do banco de dados sem estar formatoda xxxxxxxxxxx
     *
     * @return a string cpf no formato xxx.xxx.xxx-xx
     */
    public static String formatarCpf(String cpfSemFormato){

        cpfSemFormato = cpfSemFormato.replaceAll("[^0-9]", "");

        return String.format("%s.%s.%s-%s",
                cpfSemFormato.substring(0,3),
                cpfSemFormato.substring(3,6),
                cpfSemFormato.substring(6,9),
                cpfSemFormato.substring(9,11));
    }

}

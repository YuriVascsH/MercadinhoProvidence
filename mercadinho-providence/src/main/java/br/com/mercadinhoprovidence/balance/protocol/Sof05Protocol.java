package br.com.mercadinhoprovidence.balance.protocol;

import br.com.mercadinhoprovidence.balance.Weight;

import java.math.BigDecimal;

public class Sof05Protocol implements BalanceProtocol {

    private static final int TAMANHO_PESO = 5;

    private static final BigDecimal DIVISOR =
            BigDecimal.valueOf(1000);

    @Override
    public Weight parse(String response) {

        if (response == null) {
            throw new IllegalArgumentException(
                    "Resposta SOF05 não pode ser nula."
            );
        }

        if (response.length() != TAMANHO_PESO) {
            throw new IllegalArgumentException(
                    "Resposta SOF05 inválida: "
                    + response
            );
        }

        if (!response.matches("\\d{5}")) {
            throw new IllegalArgumentException(
                    "Resposta SOF05 deve conter "
                    + "5 dígitos numéricos: "
                    + response
            );
        }

        BigDecimal valor = new BigDecimal(response)
                .divide(DIVISOR);

        return new Weight(
                valor,
                true
        );
    }
}
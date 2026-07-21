package br.com.mercadinhoprovidence.balance.protocol;

package br.com.mercadinhoprovidence.balanca.protocol;

import java.math.BigDecimal;

import br.com.mercadinhoprovidence.balanca.Weight;
import br.com.mercadinhoprovidence.balanca.exception.BalanceException;

public class Sof05Protocol implements BalanceProtocol {

    private static final char STX = 0x02;
    private static final char ETX = 0x03;

    @Override
    public Weight parse(String response) {

        if (response == null || response.isBlank()) {
            throw new BalanceException("Resposta da balança está vazia.");
        }

        // Remove caracteres STX e ETX
        String cleanResponse = response
                .replace(String.valueOf(STX), "")
                .replace(String.valueOf(ETX), "")
                .trim();

        if (cleanResponse.length() != 5) {
            throw new BalanceException(
                    "Resposta inválida para o protocolo SOF05: " + cleanResponse);
        }

        try {

            // Converte o valor para kg (ex: 01250 -> 1.250 kg)
            BigDecimal value = new BigDecimal(cleanResponse)
                    .divide(new BigDecimal("1000"));

            return new Weight(value, true);

        } catch (NumberFormatException e) {

            throw new BalanceException(
                    "Não foi possível converter o peso recebido: " + cleanResponse, e);
        }
    }
}
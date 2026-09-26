package br.com.mercadinhoprovidence.balance;

import br.com.mercadinhoprovidence.balance.exception.BalanceException;
import br.com.mercadinhoprovidence.balance.protocol.BalanceProtocol;
import br.com.mercadinhoprovidence.balance.serial.SerialConnection;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

public class BalanceReader {

    private static final byte STX = 0x02;
    private static final byte ETX = 0x03;

    private final SerialConnection serialConnection;
    private final BalanceProtocol protocol;

    private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();

    public BalanceReader(
            SerialConnection serialConnection,
            BalanceProtocol protocol) {

        this.serialConnection = serialConnection;
        this.protocol = protocol;
    }

    public Weight readWeight() {

        while (true) {

            byte[] dados = serialConnection.read();

            if (dados.length == 0) {

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();

                    throw new BalanceException(
                            "Leitura da balança interrompida.");
                }

                continue;
            }

            buffer.write(
                    dados,
                    0,
                    dados.length);

            byte[] quadro = extrairQuadro();

            if (quadro != null) {

                String conteudo = new String(
                        quadro,
                        StandardCharsets.US_ASCII);

                System.out.println("Quadro recebido: [" + conteudo + "]");
                return protocol.parse(conteudo);
            }
        }
    }

    private byte[] extrairQuadro() {

        byte[] dados = buffer.toByteArray();

        int inicio = -1;
        int fim = -1;

        // Procura o STX
        for (int i = 0; i < dados.length; i++) {

            if (dados[i] == STX) {
                inicio = i;
                break;
            }
        }

        // Ainda não encontramos o início
        if (inicio == -1) {

            buffer.reset();

            return null;
        }

        // Procura o ETX depois do STX
        for (int i = inicio + 1; i < dados.length; i++) {

            if (dados[i] == ETX) {
                fim = i;
                break;
            }
        }

        // O quadro ainda está incompleto
        if (fim == -1) {
            return null;
        }

        // Extrai somente o conteúdo entre STX e ETX
        int tamanho = fim - inicio - 1;

        byte[] conteudo = new byte[tamanho];

        System.arraycopy(
                dados,
                inicio + 1,
                conteudo,
                0,
                tamanho);

        // Remove do buffer o quadro que já foi processado
        buffer.reset();

        if (fim + 1 < dados.length) {

            buffer.write(
                    dados,
                    fim + 1,
                    dados.length - fim - 1);
        }

        return conteudo;
    }
}
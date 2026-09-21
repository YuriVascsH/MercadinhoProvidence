package br.com.mercadinhoprovidence.balance;

import br.com.mercadinhoprovidence.balance.protocol.BalanceProtocol;
import br.com.mercadinhoprovidence.balance.serial.SerialConnection;

public class BalanceReader {

    private final SerialConnection serialConnection;
    private final BalanceProtocol protocol;

    public BalanceReader(
            SerialConnection serialConnection,
            BalanceProtocol protocol) {

        this.serialConnection = serialConnection;
        this.protocol = protocol;
    }

    public Weight readWeight() {

        String response = serialConnection.read();

        return protocol.parse(response);
    }
}
package br.com.mercadinhoprovidence.balance;

import br.com.mercadinhoprovidence.balance.config.BalanceConfig;
import br.com.mercadinhoprovidence.balance.protocol.BalanceProtocol;
import br.com.mercadinhoprovidence.balance.serial.SerialConnection;

public class BalanceRamuza implements Balance {

    private final SerialConnection serialConnection;
    private final BalanceReader reader;

    public BalanceRamuza(
            BalanceConfig config,
            BalanceProtocol protocol) {

        this.serialConnection = new SerialConnection(config);
        this.reader = new BalanceReader(
                serialConnection,
                protocol
        );
    }

    @Override
    public void connect() {
        serialConnection.connect();
    }

    @Override
    public void disconnect() {
        serialConnection.disconnect();
    }

    @Override
    public boolean isConnected() {
        return serialConnection.isConnected();
    }

    @Override
    public Weight readWeight() {
        return reader.readWeight();
    }
}
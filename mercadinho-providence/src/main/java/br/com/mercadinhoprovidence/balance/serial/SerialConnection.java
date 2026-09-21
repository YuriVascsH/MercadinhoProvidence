package br.com.mercadinhoprovidence.balance.serial;

import br.com.mercadinhoprovidence.balance.config.BalanceConfig;
import br.com.mercadinhoprovidence.balance.exception.BalanceException;

public class SerialConnection {

    private final BalanceConfig config;

    private boolean connected;

    public SerialConnection(BalanceConfig config) {
        this.config = config;
    }

    public void connect() {
        // Implementaremos a comunicação com jSerialComm aqui.
        connected = true;
    }

    public void disconnect() {
        // Implementaremos o fechamento da porta aqui.
        connected = false;
    }

    public boolean isConnected() {
        return connected;
    }

    public String read() {
        if (!connected) {
            throw new BalanceException("A balança não está conectada.");
        }

        // Implementaremos a leitura da porta serial aqui.
        return "";
    }

    public BalanceConfig getConfig() {
        return config;
    }
}
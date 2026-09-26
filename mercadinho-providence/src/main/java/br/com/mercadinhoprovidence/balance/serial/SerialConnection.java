package br.com.mercadinhoprovidence.balance.serial;

import br.com.mercadinhoprovidence.balance.config.BalanceConfig;
import br.com.mercadinhoprovidence.balance.exception.BalanceException;

import com.fazecast.jSerialComm.SerialPort;

public class SerialConnection {

    private final BalanceConfig config;

    private SerialPort serialPort;

    public SerialConnection(BalanceConfig config) {
        this.config = config;
    }

    public void connect() {

        if (isConnected()) {
            return;
        }

        serialPort = SerialPort.getCommPort(config.getPorta());

        serialPort.setComPortParameters(
                config.getBaudRate(),
                config.getDataBits(),
                config.getStopBits(),
                SerialPort.NO_PARITY
        );

        serialPort.setComPortTimeouts(
                SerialPort.TIMEOUT_READ_BLOCKING,
                1000,
                0
        );

        if (!serialPort.openPort()) {
            serialPort = null;

            throw new BalanceException(
                    "Não foi possível abrir a porta " + config.getPorta()
            );
        }
    }

    public void disconnect() {

        if (serialPort != null && serialPort.isOpen()) {
            serialPort.closePort();
        }

        serialPort = null;
    }

    public boolean isConnected() {

        return serialPort != null && serialPort.isOpen();
    }

    public String read() {

        if (!isConnected()) {
            throw new BalanceException(
                    "A balança não está conectada."
            );
        }

        byte[] buffer = new byte[64];

        int bytesRead = serialPort.readBytes(
                buffer,
                buffer.length
        );

        if (bytesRead <= 0) {
            return "";
        }

        return new String(
                buffer,
                0,
                bytesRead
        ).trim();
    }

    public BalanceConfig getConfig() {
        return config;
    }
}
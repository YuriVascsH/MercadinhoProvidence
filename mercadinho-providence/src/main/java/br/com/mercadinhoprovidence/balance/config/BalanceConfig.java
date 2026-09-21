package br.com.mercadinhoprovidence.balance.config;

public class BalanceConfig {

    private final String porta;
    private final int baudRate;
    private final int dataBits;
    private final int stopBits;
    private final int parity;

    public BalanceConfig(
            String porta,
            int baudRate,
            int dataBits,
            int stopBits,
            int parity) {

        this.porta = porta;
        this.baudRate = baudRate;
        this.dataBits = dataBits;
        this.stopBits = stopBits;
        this.parity = parity;
    }

    public String getPorta() {
        return porta;
    }

    public int getBaudRate() {
        return baudRate;
    }

    public int getDataBits() {
        return dataBits;
    }

    public int getStopBits() {
        return stopBits;
    }

    public int getParity() {
        return parity;
    }
}
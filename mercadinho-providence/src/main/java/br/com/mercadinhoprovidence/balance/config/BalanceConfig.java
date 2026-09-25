package br.com.mercadinhoprovidence.balance.config;

public class BalanceConfig {

    private final String porta;
    private final int baudRate;
    private final int dataBits;
    private final int stopBits;
    private final String parity;

    public BalanceConfig(String porta) {
        this.porta = porta;
        this.baudRate = 9600;
        this.dataBits = 8;
        this.stopBits = 1;
        this.parity = "none";

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

    public String getParity() {
        return parity;
    }
}
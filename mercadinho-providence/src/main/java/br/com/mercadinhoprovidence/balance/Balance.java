package br.com.mercadinhoprovidence.balance;

public interface Balance {

    void connect();

    void disconnect();

    boolean isConnected();

    Weight readWeight();

}
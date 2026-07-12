package br.com.mercadinhoprovidence.balance;

public interface Balance {

    void conectar();

    void desconectar();

    boolean estaConectada();

    Peso lerPeso();

}
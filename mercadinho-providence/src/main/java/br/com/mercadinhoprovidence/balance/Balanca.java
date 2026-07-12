package br.com.mercadinhoprovidence.balance;

public interface Balanca {

    void conectar();

    void desconectar();

    boolean estaConectada();

    Peso lerPeso();

}
package br.com.mercadinhoprovidence.balance;

import br.com.mercadinhoprovidence.balance.serial.SerialConnection;

public abstract class AbstractBalance implements Balanca {

    protected BalancaConfig config;

    protected SerialConnection serialConnection;

}
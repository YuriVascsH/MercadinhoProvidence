package br.com.mercadinhoprovidence.balance;

import br.com.mercadinhoprovidence.balance.config.BalanceConfig;
import br.com.mercadinhoprovidence.balance.serial.SerialConnection;

public abstract class AbstractBalance implements Balance {

    protected BalanceConfig config;

    protected SerialConnection serialConnection;

}
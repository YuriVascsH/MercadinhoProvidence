package br.com.mercadinhoprovidence.balance;

import br.com.mercadinhoprovidence.balance.config.BalanceConfig;
import br.com.mercadinhoprovidence.balance.serial.SerialConnection;

package br.com.mercadinhoprovidence.balanca;

import br.com.mercadinhoprovidence.balanca.serial.SerialConnection;

public abstract class AbstractBalance implements Balance {

    protected final BalanceConfig config;

    protected SerialConnection serialConnection;

    protected boolean connected;

    protected AbstractBalance(BalanceConfig config) {
        this.config = config;
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

}
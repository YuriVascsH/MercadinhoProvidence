package br.com.mercadinhoprovidence.balance.protocol;

package br.com.mercadinhoprovidence.balanca.protocol;

import br.com.mercadinhoprovidence.balanca.Weight;
import br.com.mercadinhoprovidence.balanca.exception.BalanceException;

public class Sof06Protocol implements BalanceProtocol {

    @Override
    public Weight parse(String response) {

        throw new BalanceException(
                "Protocolo SOF06 ainda não implementado.");
    }
}
package br.com.mercadinhoprovidence.balance.protocol;

package br.com.mercadinhoprovidence.balanca.protocol;

import br.com.mercadinhoprovidence.balanca.Weight;
import br.com.mercadinhoprovidence.balanca.exception.BalanceException;

public class Sof5BProtocol implements BalanceProtocol {

    @Override
    public Weight parse(String response) {

        throw new BalanceException(
                "Protocolo SOF5B ainda não implementado.");
    }
}
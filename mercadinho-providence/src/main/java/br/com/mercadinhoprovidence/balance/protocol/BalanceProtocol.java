package br.com.mercadinhoprovidence.balance.protocol;

import br.com.mercadinhoprovidence.balance.Weight;

public interface BalanceProtocol {

    Weight parse(String response);

}
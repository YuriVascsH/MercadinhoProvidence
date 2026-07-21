package br.com.mercadinhoprovidence.balance.protocol;

package br.com.mercadinhoprovidence.balanca.protocol;

import br.com.mercadinhoprovidence.balanca.Weight;

public interface BalanceProtocol {

    /**
     * Interpreta a resposta recebida da balança e converte em um objeto Weight.
     *
     * @param response resposta bruta recebida da porta serial
     * @return objeto Weight contendo o valor e o estado do peso
     */
    Weight parse(String response);

}
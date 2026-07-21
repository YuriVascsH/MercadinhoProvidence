package br.com.mercadinhoprovidence.balance;

package br.com.mercadinhoprovidence.balanca;

public interface Balance {

    /**
     * Abre a comunicação com a balança.
     *
     * @throws Exception caso a conexão não possa ser estabelecida.
     */
    void connect() throws Exception;

    /**
     * Encerra a comunicação com a balança.
     */
    void disconnect();

    /**
     * Verifica se a balança está conectada.
     *
     * @return true caso esteja conectada.
     */
    boolean isConnected();

    /**
     * Obtém o último peso lido pela balança.
     *
     * @return objeto Weight contendo as informações do peso.
     */
    Weight getWeight();

}
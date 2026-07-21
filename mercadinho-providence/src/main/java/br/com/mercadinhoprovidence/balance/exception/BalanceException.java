package br.com.mercadinhoprovidence.balance.exception;

package br.com.mercadinhoprovidence.balanca.exception;

public class BalanceException extends RuntimeException {

    public BalanceException(String message) {
        super(message);
    }

    public BalanceException(String message, Throwable cause) {
        super(message, cause);
    }

}
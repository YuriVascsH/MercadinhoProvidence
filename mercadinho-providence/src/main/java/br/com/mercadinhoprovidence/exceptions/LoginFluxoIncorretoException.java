package br.com.mercadinhoprovidence.exceptions;

public class LoginFluxoIncorretoException extends IllegalArgumentException{
    public LoginFluxoIncorretoException(String message) {
        super(message);
    }
}

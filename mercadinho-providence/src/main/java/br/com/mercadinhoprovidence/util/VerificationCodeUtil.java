package br.com.mercadinhoprovidence.util;

import java.security.SecureRandom;

public final class VerificationCodeUtil {

    private static final SecureRandom random = new SecureRandom();

    public VerificationCodeUtil() {
        throw new UnsupportedOperationException("Classe utilitária não pode ser instanciada.");
    }

    /**
     * Função para gerar o código verificador do funcionário para realizar o seu
     * acesso ao sistema
     *
     * @return o código verificador do funcionário
     */
    public static int generateVerificationCode() {
        return 1000000000 + random.nextInt(Integer.MAX_VALUE - 1000000000);
    }
}
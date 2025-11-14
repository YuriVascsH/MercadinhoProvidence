package br.com.mercadinhoprovidence.util;

import java.security.SecureRandom;

public class CodigoVerificadorUtil {

    private static final SecureRandom random = new SecureRandom();

    /**
     * Função para gerar o código verificador do funcionário para realizar o seu acesso ao sistema
     *
     * @return o código verificador do funcionário
     * */
    public static int gerarCodigoVerificador() {
        return 1000000000 + random.nextInt(Integer.MAX_VALUE - 1000000000);
    }
}
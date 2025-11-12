package br.com.mercadinhoprovidence.model.enums;


public enum Forma {
    
    DINHEIRO("Dinheiro"),
    PIX("Pix"),
    CARTAO_DEBITO("Débito"),
    CARTAO_CREDITO("Crédito"),
    VA("Vale Alimentação"), 
    VR("Vale Refeição");    

    private String forma;
    
    private Forma(String forma) {
        this.forma = forma;
    }

    /**
     * Retorna a representação em String do enum. Esta é a string
     * que será inserida na coluna 'forma' da tabela PAGAMENTOS.
     */
    @Override
    public String toString() {
        return forma;
    }

    /**
     * Converte uma String para o enum correspondente, ignorando
     * a capitalização. Útil para buscas na UI.
     * @param text A string a ser convertida.
     * @return O enum Forma correspondente.
     * @throws IllegalArgumentException se a string não for encontrada.
     */
    public static Forma fromString(String text) {
        for (Forma f : Forma.values()) {
            // Compara a string recebida (ex: "debito") com a string interna do enum ("Débito"),
            // ignorando a capitalização.
            if (f.toString().equalsIgnoreCase(text)) {
                return f;
            }
        }
        throw new IllegalArgumentException("Forma de pagamento inválida: " + text);
    }
}

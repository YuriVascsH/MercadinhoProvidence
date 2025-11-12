package br.com.mercadinhoprovidence.model.enums;

public enum Categoria {

    GERAL("Geral", "UN"),    
    AVULSOS("Avulsos", "UN"),  
    HORTI("Horti", "KG");    

    private String nomeCategoria;
    private String unidadePadrao; // Novo campo

    Categoria(String nomeCategoria, String unidadePadrao) {
        this.nomeCategoria = nomeCategoria;
        this.unidadePadrao = unidadePadrao;
    }
    
    // NOVO GETTER
    public String getUnidadePadrao() {
        return unidadePadrao;
    }
    
    @Override
    public String toString() {
        return nomeCategoria;
    }
    
    public static Categoria fromString(String text) {
        for (Categoria c: Categoria.values()) {
            if(c.nomeCategoria.equalsIgnoreCase(text)) { 
                return c;
            }
        }
        throw new IllegalArgumentException("Categoria inválida:" + text);
    }
}
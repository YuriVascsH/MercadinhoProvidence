package br.com.mercadinhoprovidence.model.enums;

public enum Category {
    UNIDADE("Unidade", "UN"),
    PESO("Peso", "Kg");

    private final String nameCategory;
    private final String unit;

    Category(String nameCategory, String unit) {
        this.nameCategory = nameCategory;
        this.unit = unit;
    }

    public String getUnit() {
        return unit;
    }

    @Override
    public String toString() {
        return nameCategory;
    }

    public static Category fromString(String text) {
        for (Category c : Category.values()) {
            if (c.nameCategory.equalsIgnoreCase(text)) {
                return c;
            }
        }
        throw new IllegalArgumentException("Categoria inválida: " + text);
    }
}

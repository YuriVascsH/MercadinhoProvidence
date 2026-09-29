package br.com.mercadinhoprovidence.model.enums;

import lombok.Getter;

@Getter
public enum SellStatus {

    IN_PROGRESS("Em andamento"),
    COMPLETED("Concluída"),
    CANCELED("Cancelada");

    private final String description;

    SellStatus(String description) {
        this.description = description;
    }

    public static SellStatus fromString(String text) {
        for (SellStatus status : SellStatus.values()) {
            if (status.name().equalsIgnoreCase(text) || status.description.equalsIgnoreCase(text)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Status inválido: " + text);
    }

    @Override
    public String toString() {
        return description;
    }
}
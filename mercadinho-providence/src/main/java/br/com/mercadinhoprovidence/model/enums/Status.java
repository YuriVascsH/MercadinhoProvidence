package br.com.mercadinhoprovidence.model.enums;

public enum Status {
	EM_ANDAMENTO("Em andamento"),
	FINALIZADO("Finalizado"),
	CANCELADO("Cancelado"),
	REALIZADO("Realizado"),
	AGUARDANDO_PAGAMENTO("Aguardando pagamento"),
	PAGO("Pago");

	private String status;

	Status(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return status;
	}

	public static Status fromString(String text) {
		for (Status s : Status.values()) {
			if (s.toString().equalsIgnoreCase(text)) {
				return s;
			}
		}
		throw new IllegalArgumentException("Status inválido: " + text);
	}
}

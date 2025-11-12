package br.com.mercadinhoprovidence.model.enums;

public enum Cargo {
	
	OPERADOR("Operador"),
	GERENTE("Gerente");

	private String cargo;
	
	Cargo(String cargo) {
		this.cargo = cargo;
	}

	@Override
	public String toString() {
		return cargo;
	}

	public static Cargo fromString(String text) {
		for (Cargo c : Cargo.values()) {
			if (c.toString().equalsIgnoreCase(text)) {
				return c;
			}
		}
		throw new IllegalArgumentException("Cargo inválido: " + text);
	}
}

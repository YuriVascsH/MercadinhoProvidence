package br.com.mercadinhoprovidence.model.enums;

public enum JobTitle {
	
	OPERADOR("Operador"),
	GERENTE("Gerente");

	private String jobTitle;
	
	JobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	@Override
	public String toString() {
		return jobTitle;
	}

	public static JobTitle fromString(String text) {
		for (JobTitle c : JobTitle.values()) {
			if (c.toString().equalsIgnoreCase(text)) {
				return c;
			}
		}
		throw new IllegalArgumentException("Cargo inválido: " + text);
	}
}

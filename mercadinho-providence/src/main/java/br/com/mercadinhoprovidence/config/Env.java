package br.com.mercadinhoprovidence.config;

import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.util.logging.*;

public class Env {
	private static final Logger logger = Logger.getLogger(Env.class.getName());
	private static final Dotenv dotenv = loadDotenv();

	static {
		try {
			// Cria o diretório de logs se necessário
			java.nio.file.Path logDir = java.nio.file.Paths.get("logs");
			if (!java.nio.file.Files.exists(logDir)) {
				java.nio.file.Files.createDirectory(logDir);
			}

			// Configura o FileHandler
			FileHandler fileHandler = new FileHandler("logs/env.log", true); // 'true' para append
			fileHandler.setFormatter(new SimpleFormatter());
			fileHandler.setLevel(Level.ALL);

			logger.addHandler(fileHandler);
			logger.setLevel(Level.ALL);
			logger.setUseParentHandlers(false); // evita duplicação no console
		} catch (IOException e) {
			System.err.println("Não foi possível configurar o FileHandler para logs: " + e.getMessage());
		}
	}

	private static Dotenv loadDotenv() {
		Dotenv env;

		try {
			
			// 1. Tenta carregar diretamente de src/main/resources (desenvolvimento)
	        env = Dotenv.configure()
	                .directory("src/main/resources/.env")
	                .ignoreIfMalformed()
	                .ignoreIfMissing()
	                .load();

	        if (!env.entries().isEmpty()) {
	            logger.info("Arquivo .env carregado de src/main/resources");
	            return env;
	        }

			// 2. Tenta carregar da raiz do projeto (produção)

			String prodPath = System.getProperty("user.dir") + "/resources";
			env = Dotenv.configure().directory(prodPath).ignoreIfMalformed().ignoreIfMissing().load();

			if (!env.entries().isEmpty()) {
				logger.info("Arquivo .env carregado de " + prodPath);
				return env;
			}

			logger.warning("Nenhum arquivo .env encontrado nos diretórios esperados.");
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Erro ao carregar arquivo .env", e);
		}
        
		// Retorna um Dotenv vazio para evitar falhas
		return Dotenv.configure().ignoreIfMalformed().ignoreIfMissing().load();
	}

	public static String get(String key) {
		String value = dotenv.get(key);
		if (value == null) {
			logger.warning("Variável de ambiente '" + key + "' não encontrada.");
		}
		return value;
	}
}

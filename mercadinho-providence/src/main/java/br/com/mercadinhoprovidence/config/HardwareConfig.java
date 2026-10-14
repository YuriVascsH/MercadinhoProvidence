package br.com.mercadinhoprovidence.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class HardwareConfig {

    private static final Properties PROPERTIES = new Properties();

    private static final Path CONFIG_PATH = localizarArquivo();

    static {
        carregar();
    }

    private HardwareConfig() {
    }

    private static Path localizarArquivo() {
        String caminho = System.getProperty("pdv.config");

        if (caminho != null && !caminho.isBlank()) {
            return Path.of(caminho).toAbsolutePath();
        }

        return Path.of(
            System.getProperty("user.dir"),
            "config.properties"
        ).toAbsolutePath();
    }

    private static void carregar() {
        if (!Files.isRegularFile(CONFIG_PATH)) {
            throw new IllegalStateException(
                "Arquivo de configuração não encontrado: "
                + CONFIG_PATH
            );
        }

        try (InputStream input =
                 Files.newInputStream(CONFIG_PATH)) {

            PROPERTIES.load(input);

        } catch (IOException e) {
            throw new IllegalStateException(
                "Não foi possível ler o arquivo de configuração: "
                + CONFIG_PATH,
                e
            );
        }
    }

    private static String obterObrigatorio(String chave) {
        String valor = PROPERTIES.getProperty(chave);

        if (valor == null || valor.isBlank()) {
            throw new IllegalStateException(
                "Configuração ausente ou vazia: " + chave
            );
        }

        return valor.trim();
    }

    public static String getPortaImpressora() {
        return obterObrigatorio("hardware.impressora.porta");
    }

    public static String getPortaBalanca() {
        return obterObrigatorio("hardware.balanca.porta");
    }
}
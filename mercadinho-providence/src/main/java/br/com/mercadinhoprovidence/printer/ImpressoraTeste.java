package br.com.mercadinhoprovidence.printer;

import com.fazecast.jSerialComm.SerialPort;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ImpressoraTeste {

    public static void listarCharsetsSuportados() {
        System.out.println("Charsets suportados na JVM:");
        Charset.availableCharsets().keySet().forEach(System.out::println);
    }

    public static void testeMultiplo(String porta) {
        SerialPort comPort = SerialPort.getCommPort(porta);

        // ajuste para 19200 (tente esse primeiro — corresponde ao app)
        comPort.setComPortParameters(19200, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0);

        if (!comPort.openPort()) {
            System.err.println("Não abriu porta " + porta);
            return;
        }

        try (OutputStream out = comPort.getOutputStream()) {
            String sample = "Teste acentuação: á é í ó ú ã õ ç Á É Ô Ç\n";

            // reset
            out.write(new byte[]{0x1B, 0x40}); // ESC @
            out.flush();

            for (int n = 0; n <= 15; n++) {
                // solicita que a impressora selecione a tabela 'n'
                out.write(new byte[]{0x1B, 0x74, (byte) n}); // ESC t n
                out.flush();

                // Imprime rótulo com índice (uso CP437 para rótulo ASCII seguro)
                try { out.write(("=== ESC t " + n + " ===\n").getBytes("CP437")); } catch(Exception e){ out.write(("=== ESC t " + n + " ===\n").getBytes()); }

                // Tenta várias codificações (cada tentativa em linha separada)
                try { out.write(("CP860 : ").getBytes("CP437")); out.write(sample.getBytes("CP860")); out.write("\n".getBytes()); } catch(Exception ignored) {}
                try { out.write(("IBM860: ").getBytes("CP437")); out.write(sample.getBytes("IBM860")); out.write("\n".getBytes()); } catch(Exception ignored) {}
                try { out.write(("CP850 : ").getBytes("CP437")); out.write(sample.getBytes("CP850")); out.write("\n".getBytes()); } catch(Exception ignored) {}
                try { out.write(("CP1252: ").getBytes("CP437")); out.write(sample.getBytes("CP1252")); out.write("\n".getBytes()); } catch(Exception ignored) {}
                try { out.write(("UTF-8 : ").getBytes("CP437")); out.write(sample.getBytes(StandardCharsets.UTF_8)); out.write("\n".getBytes()); } catch(Exception ignored) {}

                // avança 2 linhas para separar blocos
                out.write(new byte[]{0x1B, 0x64, 0x02}); // ESC d 2
                out.flush();
                Thread.sleep(120); // pequeno delay entre blocos
            }

            // corte
            out.write(new byte[]{0x1D, 0x56, 0x01}); // GS V 1
            out.flush();

            System.out.println("Teste enviado — verifique o cupom impresso para ver qual linha ficou boa.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            comPort.closePort();
        }
    }

    public static void main(String[] args) {
        listarCharsetsSuportados(); // útil para ver quais nomes de charset a JVM conhece
        testeMultiplo("COM4");
    }
}


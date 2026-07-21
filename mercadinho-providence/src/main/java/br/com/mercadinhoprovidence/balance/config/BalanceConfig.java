package br.com.mercadinhoprovidence.balance.config;

@Getter
@Setter
@NoArgsConstructor
public class BalanceConfig {

    private String portName = "COM4";

    private int baudRate = 9600;

    private int dataBits = 8;

    private int stopBits = 1;

    private int parity = SerialPort.NO_PARITY;

}
package br.com.mercadinhoprovidence;


import java.util.HashSet;
import java.util.Set;

import br.com.mercadinhoprovidence.controller.LoginController;
import br.com.mercadinhoprovidence.dto.login.LoginResponseDto;
import br.com.mercadinhoprovidence.model.Funcionario;
import br.com.mercadinhoprovidence.model.enums.Cargo;
import br.com.mercadinhoprovidence.view.TelaCodigoVerificador;
import br.com.mercadinhoprovidence.view.TelaInicialView;
import br.com.mercadinhoprovidence.view.TelaLogin;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lombok.Getter;

public class MainApplication extends Application {

    //
    //    /**
    //     * Fecha a aplicação
    //     */
    //    public void fecharAplicacao() {
    //        primaryStage.close();
    //    }
    //
    @Getter
    private Stage primaryStage;

    private LoginResponseDto funcionarioLogadoNaSessao;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;

        // Teste para sabino
        // Produto produtoTeste = new Produto();
        // produtoTeste.setIdProduto(1);
        // produtoTeste.setNome("Arroz Agulhinha Tipo 1 - 5kg");
        // produtoTeste.setPrecoVenda(25.99);

        // ItemVenda item = new ItemVenda(produtoTeste, 1);

        // Venda vendaTeste = new Venda(
        //     999,
        //     LocalDateTime.now(),
        //     25.99,
        //     0.0,
        //     25.99,
        //     1000
        // );

        // Pagamento pagamentoTeste = new Pagamento();
        // pagamentoTeste.setForma(Forma.CARTAO_DEBITO);
        // pagamentoTeste.setValorPago(25.99);
        // pagamentoTeste.setTroco(0.0);
        // vendaTeste.setPagamento(pagamentoTeste);
        // vendaTeste.getItensVenda().add(item);
        // Impressora.imprimirCupom(vendaTeste);

        // // Print do tamanho da tela
        // Rectangle2D totalScreenBounds = Screen.getPrimary().getBounds();
        // double totalScreenWidth = totalScreenBounds.getWidth();
        // double totalScreenHeight = totalScreenBounds.getHeight();

        // System.out.println("------------------------------------");
        // System.out.println("Tamanho tt do monitor");
        // System.out.println("Largura Total: " + totalScreenWidth + " pixels");
        // System.out.println("Altura Total: " + totalScreenHeight + " pixels");
        // System.out.println("------------------------------------");

        // System.out.println("------------------------------------");
        // System.out.println("Tamanho visivel");
        // Rectangle2D visualScreenBounds = Screen.getPrimary().getVisualBounds();
        // System.out.println("Largura Visível: " + visualScreenBounds.getWidth() + "
        // pixels");
        // System.out.println("Altura Visível: " + visualScreenBounds.getHeight() + "
        // pixels");
        // System.out.println("------------------------------------");

        mostrarTelaLogin();
    }

    /**
     * Exibe a Tela de Login.
     */
    public void mostrarTelaLogin() {
        TelaLogin telaLogin = new TelaLogin(this);
        primaryStage.setTitle("Mercadinho Providence - Login");
        primaryStage.setScene(telaLogin.getScene());
        primaryStage.setFullScreen(false);
        primaryStage.sizeToScene();
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    /**
     * Exibe a Tela de Código Verificador para a segunda etapa do login.
     *
     * @param loginController O LoginController com o estado da primeira etapa.
     */
    public void mostrarTelaCodigoVerificador(LoginController loginController) {
        TelaCodigoVerificador telaCodigoVerificador = new TelaCodigoVerificador(this, loginController);
        primaryStage.setTitle("Mercadinho Providence - Verificação de Código");
        primaryStage.setScene(telaCodigoVerificador.getScene());
        primaryStage.setFullScreen(false);
        primaryStage.sizeToScene();
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    /**
     * Exibe a Tela Inicial após o login.
     *
     * @param funcionarioLogado O funcionário que realizou o login.
     */

    public void mostrarTelaInicial(LoginResponseDto funcionarioLogado) {
        Set<String> botoesDesabilitados = new HashSet<>();
        if (funcionarioLogado != null && funcionarioLogado.getCargo() == Cargo.OPERADOR) {
            botoesDesabilitados.add("Relatorio");
            botoesDesabilitados.add("Funcionarios");
        }

        this.funcionarioLogadoNaSessao = funcionarioLogado;

        TelaInicialView telaInicialView = new TelaInicialView(
                this, funcionarioLogado, botoesDesabilitados, this::mostrarTelaVenda);

        primaryStage.setTitle("Mercadinho Providence PDV");
        primaryStage.setScene(telaInicialView.getScene());

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        primaryStage.setX(screenBounds.getMinX());
        primaryStage.setY(screenBounds.getMinY());
        primaryStage.setWidth(screenBounds.getWidth());
        primaryStage.setHeight(screenBounds.getHeight());

        // Impede redimensionamento se quiser
        primaryStage.setResizable(false);

        primaryStage.show();
    }

    /**
     * Exibe a Tela de Venda (PDV) em tela cheia.
     */
    public void mostrarTelaVenda(LoginResponseDto funcionarioParaVenda) {
//        VendaController vendaController = new VendaController();
//
//        vendaController.iniciarNovaVenda(funcionarioParaVenda.getIdFuncionario());
//
//        TelaVendaView telaVendaView = new TelaVendaView(
//                this.primaryStage,
//                (f) -> this.mostrarTelaInicial(f),
//                funcionarioParaVenda,
//                vendaController
//        );
//
//        Scene pdvScene = new Scene(telaVendaView, 1000, 700);
//        pdvScene.getStylesheets().add(getClass().getResource("/styles/pdvStyle.css").toExternalForm());
//
//        primaryStage.setTitle("Mercadinho Providence PDV - Venda");
//        primaryStage.setScene(pdvScene);
//        primaryStage.setFullScreen(true);
//        primaryStage.show();
//        System.out.println(funcionarioParaVenda);
    }

    public static void main(String[] args) {
       launch(args);

   }
}

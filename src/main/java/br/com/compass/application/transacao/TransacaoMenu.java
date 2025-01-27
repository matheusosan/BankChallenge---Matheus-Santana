package br.com.compass.application.transacao;

import br.com.compass.application.transacao.services.TransacaoService;

import java.math.BigDecimal;
import java.util.Scanner;
import java.util.UUID;

public class TransacaoMenu {

    private final TransacaoService transacaoService;
    private final Scanner scanner;

    public TransacaoMenu(TransacaoService transacaoService) {
        this.transacaoService = transacaoService;
        this.scanner = new Scanner(System.in);
    }

    public void iniciarDeposito(UUID contaAutenticada) {
        try {
            System.out.print("Digite o valor do depósito: ");
            String valorInput = scanner.nextLine();
            BigDecimal valorDeposito = new BigDecimal(valorInput);

            transacaoService.realizarDeposito(contaAutenticada.toString(), valorDeposito);
            System.out.println("Depósito realizado com sucesso!");

        } catch (RuntimeException e) {
            System.out.println("Erro ao realizar depósito: " + e.getMessage());
        }
    }

    public void iniciarSaque(UUID contaAutenticada) {
        try {
            System.out.print("Digite o valor do saque: ");
            String valorInput = scanner.nextLine();
            BigDecimal valorSaque = new BigDecimal(valorInput);

            transacaoService.realizarSaque(contaAutenticada.toString(), valorSaque);

            System.out.println("Saque realizado com sucesso!");
        } catch (RuntimeException e) {
            System.out.println("Erro ao realizar saque: " + e.getMessage());
        }
    }

    public void iniciarTransferencia(UUID contaAutenticada) {
        try {
            System.out.print("Digite o número da conta de destino: ");
            String contaDestinoId = scanner.nextLine();

            System.out.print("Digite o valor da transferência: ");
            String valorInput = scanner.nextLine();
            BigDecimal montante = new BigDecimal(valorInput);

            transacaoService.realizarTransferencia(contaDestinoId, contaAutenticada.toString(), montante);

            System.out.println("Transferência realizada com sucesso!");

        } catch (RuntimeException e) {
            System.out.println("Erro ao realizar transferência: " + e.getMessage());
        }
    }


}
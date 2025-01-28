package br.com.compass.application.transacao;

import br.com.compass.application.transacao.services.TransactionService;

import java.math.BigDecimal;
import java.util.Scanner;
import java.util.UUID;

public class TransacaoMenu {

    private final TransactionService transactionService;
    private final Scanner scanner;

    public TransacaoMenu(TransactionService transactionService) {
        this.transactionService = transactionService;
        this.scanner = new Scanner(System.in);
    }

    public void iniciarDeposito(UUID contaAutenticada) {
        try {
            System.out.print("Digite o valor do depósito: ");
            String valorInput = scanner.nextLine();
            BigDecimal valorDeposito = new BigDecimal(valorInput);

            transactionService.realizarDeposito(contaAutenticada.toString(), valorDeposito);
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

            transactionService.realizarSaque(contaAutenticada.toString(), valorSaque);

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

            transactionService.realizarTransferencia(contaDestinoId, contaAutenticada.toString(), montante);

            System.out.println("Transferência realizada com sucesso!");

        } catch (RuntimeException e) {
            System.out.println("Erro ao realizar transferência: " + e.getMessage());
        }
    }


}
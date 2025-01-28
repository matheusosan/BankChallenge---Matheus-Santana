package br.com.compass.application.transacao;

import br.com.compass.application.transacao.services.TransactionService;

import java.math.BigDecimal;
import java.util.Scanner;
import java.util.UUID;

public class TransactionMenu {

    private final TransactionService transactionService;
    private final Scanner scanner;

    public TransactionMenu(TransactionService transactionService) {
        this.transactionService = transactionService;
        this.scanner = new Scanner(System.in);
    }

    public void makeDeposit(UUID authenticatedAccountId) {
        try {
            System.out.print("Digite o valor do depósito: ");
            String amountInput = scanner.nextLine();
            BigDecimal amount = new BigDecimal(amountInput);

            transactionService.makeDeposit(authenticatedAccountId.toString(), amount);
            System.out.println("Depósito realizado com sucesso!");

        } catch (RuntimeException e) {
            System.out.println("Erro ao realizar depósito: " + e.getMessage());
        }
    }

    public void makeWithdraw(UUID authenticatedAccountId) {
        try {
            System.out.print("Digite o valor do saque: ");
            String amountInput = scanner.nextLine();
            BigDecimal amount = new BigDecimal(amountInput);

            transactionService.makeWithdraw(authenticatedAccountId.toString(), amount);

            System.out.println("Saque realizado com sucesso!");
        } catch (RuntimeException e) {
            System.out.println("Erro ao realizar saque: " + e.getMessage());
        }
    }

    public void makeTransfer(UUID authenticatedAccountId) {
        try {
            System.out.print("Digite o número da conta de destino: ");
            String destinyAccountId = scanner.nextLine();

            System.out.print("Digite o valor da transferência: ");
            String amountInput = scanner.nextLine();
            BigDecimal amount = new BigDecimal(amountInput);

            transactionService.makeTransfer(destinyAccountId, authenticatedAccountId.toString(), amount);

            System.out.println("Transferência realizada com sucesso!");

        } catch (RuntimeException e) {
            System.out.println("Erro ao realizar transferência: " + e.getMessage());
        }
    }


}
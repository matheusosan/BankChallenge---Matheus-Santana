package br.com.compass.application.account;

import br.com.compass.application.account.services.AccountService;
import br.com.compass.domain.entities.Account;
import br.com.compass.domain.entities.Transaction;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.UUID;

public class AccountMenu {
    private final AccountService accountService;
    private final Scanner scanner;

    public AccountMenu(AccountService accountService) {
        this.accountService = accountService;
        this.scanner = new Scanner(System.in);
    }

    public void createAccount() {
        try {
            System.out.print("Digite o nome: ");
            String name = scanner.nextLine();

            System.out.print("Digite a data de nascimento (formato: dd-MM-yyyy): ");
            String birthDateInput = scanner.nextLine();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate birthDate = LocalDate.parse(birthDateInput, formatter);

            System.out.print("Digite o CPF: ");
            String cpf = scanner.nextLine();

            System.out.print("Crie uma senha: ");
            String password = scanner.nextLine();

            System.out.print("Digite o número de telefone: ");
            String phoneNumber = scanner.nextLine();

            System.out.println("Escolha o tipo de conta:");
            System.out.println("1 - Conta Corrente");
            System.out.println("2 - Conta Salário");
            System.out.println("3 - Conta Poupança");
            System.out.print("Digite a opção desejada (1, 2 ou 3): ");
            int accountOption = scanner.nextInt();
            scanner.nextLine();

            Account.AccountType accountType;
            switch (accountOption) {
                case 1 -> accountType = Account.AccountType.CONTA_CORRENTE;
                case 2 -> accountType =  Account.AccountType.CONTA_SALARIO;
                case 3 -> accountType =  Account.AccountType.CONTA_POUPANCA;
                default -> throw new IllegalArgumentException("Opção inválida para tipo de conta.");
            }

            accountService.createAccount(name, birthDate, cpf, password, phoneNumber, accountType);
            System.out.println("Conta salva com sucesso!");

        } catch (RuntimeException e) {
            System.out.println("Erro ao criar conta: " + e.getMessage());
        }
    }

    public UUID login() {
        UUID accountId = null;

        try {
            System.out.print("Digite seu CPF para realizar login: ");
            String cpf = scanner.nextLine();

            System.out.print("Digite sua senha: ");
            String password = scanner.nextLine();

            accountId = accountService.login(password, cpf);

            System.out.println("Login bem sucedido!");
        } catch (RuntimeException e) {
            System.out.println("Ocorreu um erro: " + e.getMessage());
        }
        return accountId;
    };

    public void checkBalance(UUID authenticatedAccount) {
        try {
            BigDecimal balance = accountService.checkBalance(authenticatedAccount.toString());
            NumberFormat formatToBrl = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

            String formattedValue = formatToBrl.format(balance);

            System.out.println("Valor em conta: " + formattedValue);
        }
        catch (RuntimeException e) {
                System.out.println("Ocorreu um erro ao visualizar saldo: " + e.getMessage());
            }
        }

    public void checkStatement(UUID authenticatedAccount) {
        try {
            List<Transaction> transactions = accountService.checkStatement(authenticatedAccount.toString());

            System.out.println("Transações da conta:");
                for (Transaction transaction : transactions) {
                    System.out.printf(
                            "Tipo: %s, Quantia: %s, Data: %s%n",
                            transaction.getTransactionType(),
                            transaction.getAmount(),
                            transaction.getCreatedAt()
                    );
                }

        } catch (RuntimeException e) {
            System.out.println("Erro ao buscar transações: " + e.getMessage());
        }
    }

}

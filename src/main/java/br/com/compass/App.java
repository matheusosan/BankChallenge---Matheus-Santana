package br.com.compass;

import br.com.compass.application.account.AccountMenu;
import br.com.compass.application.account.services.AccountService;
import br.com.compass.application.security.BCryptService;
import br.com.compass.application.security.IEncryptionService;
import br.com.compass.application.transacao.TransactionMenu;
import br.com.compass.application.transacao.services.TransactionService;

import java.util.Scanner;
import java.util.UUID;

public class App {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        mainMenu(scanner);
        
        scanner.close();
        System.out.println("Application closed");
    }

    public static void mainMenu(Scanner scanner) {
        IEncryptionService encryptionService = new BCryptService();
        AccountService accountService = new AccountService(encryptionService);
        AccountMenu accountMenu = new AccountMenu(accountService);

        boolean running = true;

        while (running) {
            System.out.println("========= Main Menu =========");
            System.out.println("|| 1. Login                ||");
            System.out.println("|| 2. Account Opening      ||");
            System.out.println("|| 0. Exit                 ||");
            System.out.println("=============================");
            System.out.print("Choose an option: ");

            int option = scanner.nextInt();

            switch (option) {
                case 1:
                    UUID accountId = accountMenu.login();
                    if(accountId == null) {
                        break;
                    }
                    bankMenu(scanner, accountId);
                    return;
                case 2:
                    accountMenu.createAccount();
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option! Please try again.");
            }
        }
    }

    public static void bankMenu(Scanner scanner, UUID contaLogada) {
        TransactionService transactionService = new TransactionService();
        TransactionMenu transactionMenu = new TransactionMenu(transactionService);
        IEncryptionService encryptionService = new BCryptService();
        AccountService accountService = new AccountService(encryptionService);
        AccountMenu accountMenu = new AccountMenu(accountService);

        boolean running = true;

        while (running) {
            System.out.println("========= Bank Menu =========");
            System.out.println("|| 1. Deposit              ||");
            System.out.println("|| 2. Withdraw             ||");
            System.out.println("|| 3. Check Balance        ||");
            System.out.println("|| 4. Transfer             ||");
            System.out.println("|| 5. Bank Statement       ||");
            System.out.println("|| 0. Exit                 ||");
            System.out.println("=============================");
            System.out.print("Choose an option: ");

            int option = scanner.nextInt();

            switch (option) {
                case 1:
                    transactionMenu.makeDeposit(contaLogada);
                    break;
                case 2:
                    transactionMenu.makeWithdraw(contaLogada);
                    break;
                case 3:
                    accountMenu.checkBalance(contaLogada);
                    break;
                case 4:
                    transactionMenu.makeTransfer(contaLogada);
                    break;
                case 5:
                    accountMenu.checkStatement(contaLogada);
                    break;
                case 0:
                    System.out.println("Exiting...");
                    running = false;
                    return;
                default:
                    System.out.println("Invalid option! Please try again.");
            }
        }
    }
    
}

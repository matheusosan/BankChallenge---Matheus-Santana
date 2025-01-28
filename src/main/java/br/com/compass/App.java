package br.com.compass;

import br.com.compass.application.conta.ContaMenu;
import br.com.compass.application.conta.services.AccountService;
import br.com.compass.application.security.BCryptService;
import br.com.compass.application.security.ICriptografiaService;
import br.com.compass.application.transacao.TransacaoMenu;
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
        ICriptografiaService criptografiaService = new BCryptService();
        AccountService accountService = new AccountService(criptografiaService);
        ContaMenu contaMenu = new ContaMenu(accountService);

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
                    UUID contaId = contaMenu.realizarLogin();
                    if(contaId == null) {
                        break;
                    }
                    bankMenu(scanner, contaId);
                    return;
                case 2:
                    contaMenu.iniciarCriacaoConta();
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
        TransacaoMenu transacaoMenu = new TransacaoMenu(transactionService);
        ICriptografiaService criptografiaService = new BCryptService();
        AccountService accountService = new AccountService(criptografiaService);
        ContaMenu contaMenu = new ContaMenu(accountService);

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
                    transacaoMenu.iniciarDeposito(contaLogada);
                    break;
                case 2:
                    transacaoMenu.iniciarSaque(contaLogada);
                    break;
                case 3:
                    contaMenu.verificarSaldo(contaLogada);
                    break;
                case 4:
                    transacaoMenu.iniciarTransferencia(contaLogada);
                    break;
                case 5:
                    contaMenu.iniciarConsultaTransacoes(contaLogada);
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

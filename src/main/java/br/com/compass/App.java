package br.com.compass;

import br.com.compass.application.conta.ContaMenu;
import br.com.compass.application.conta.services.ContaService;
import br.com.compass.application.transacao.TransacaoMenu;
import br.com.compass.application.transacao.services.TransacaoService;

import java.util.Scanner;

public class App {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        mainMenu(scanner);
        
        scanner.close();
        System.out.println("Application closed");
    }

    public static void mainMenu(Scanner scanner) {
        ContaService contaService = new ContaService();
        ContaMenu contaMenu = new ContaMenu(contaService);

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
                    bankMenu(scanner);
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

    public static void bankMenu(Scanner scanner) {
        TransacaoService transacaoService = new TransacaoService();
        TransacaoMenu transacaoMenu = new TransacaoMenu(transacaoService);
        ContaService contaService = new ContaService();
        ContaMenu contaMenu = new ContaMenu(contaService);

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
                    transacaoMenu.iniciarDeposito();
                    break;
                case 2:
                    transacaoMenu.iniciarSaque();
                    break;
                case 3:
                    contaMenu.verificarSaldo();
                    break;
                case 4:
                    transacaoMenu.iniciarTransferencia();
                    break;
                case 5:
                    contaMenu.iniciarConsultaTransacoes();
                    break;
                case 0:
                    // ToDo...
                    System.out.println("Exiting...");
                    running = false;
                    return;
                default:
                    System.out.println("Invalid option! Please try again.");
            }
        }
    }
    
}

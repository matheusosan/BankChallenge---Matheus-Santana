package br.com.compass.application.conta;

import br.com.compass.application.conta.services.AccountService;
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

public class ContaMenu {
    private final AccountService accountService;
    private final Scanner scanner;

    public ContaMenu(AccountService accountService) {
        this.accountService = accountService;
        this.scanner = new Scanner(System.in);
    }

    public void iniciarCriacaoConta() {
        try {
            System.out.print("Digite o nome: ");
            String nome = scanner.nextLine();

            System.out.print("Digite a data de nascimento (formato: dd-MM-yyyy): ");
            String dataNascimentoInput = scanner.nextLine();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate dataNascimento = LocalDate.parse(dataNascimentoInput, formatter);

            System.out.print("Digite o CPF: ");
            String cpf = scanner.nextLine();

            System.out.print("Crie uma senha: ");
            String senha = scanner.nextLine();

            System.out.print("Digite o número de telefone: ");
            String numeroTelefone = scanner.nextLine();

            System.out.println("Escolha o tipo de conta:");
            System.out.println("1 - Conta Corrente");
            System.out.println("2 - Conta Salário");
            System.out.println("3 - Conta Poupança");
            System.out.print("Digite a opção desejada (1, 2 ou 3): ");
            int opcao = scanner.nextInt();
            scanner.nextLine();

            Account.AccountType tipoConta;
            switch (opcao) {
                case 1 -> tipoConta = Account.AccountType.CONTA_CORRENTE;
                case 2 -> tipoConta =  Account.AccountType.CONTA_SALARIO;
                case 3 -> tipoConta =  Account.AccountType.CONTA_POUPANCA;
                default -> throw new IllegalArgumentException("Opção inválida para tipo de conta.");
            }

            accountService.criarConta(nome, dataNascimento, cpf, senha, numeroTelefone, tipoConta);
            System.out.println("Conta salva com sucesso!");

        } catch (RuntimeException e) {
            System.out.println("Erro ao criar conta: " + e.getMessage());
        }
    }

    public UUID realizarLogin() {
        UUID idConta = null;

        try {
            System.out.print("Digite seu CPF para realizar login: ");
            String cpf = scanner.nextLine();

            System.out.print("Digite sua senha: ");
            String senha = scanner.nextLine();

            idConta = accountService.realizarLogin(senha, cpf);

            System.out.println("Login bem sucedido!");
        } catch (RuntimeException e) {
            System.out.println("Ocorreu um erro: " + e.getMessage());
        }
        return idConta;
    };

    public void verificarSaldo(UUID contaAutenticada) {
        try {
            BigDecimal balance = accountService.verificarSaldo(contaAutenticada.toString());
            NumberFormat formatToBrl = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

            String formattedValue = formatToBrl.format(balance);

            System.out.println("Valor em conta: " + formattedValue);
        }
        catch (RuntimeException e) {
                System.out.println("Ocorreu um erro ao visualizar saldo: " + e.getMessage());
            }
        }

    public void iniciarConsultaTransacoes(UUID contaAutenticada) {
        try {
            List<Transaction> transactions = accountService.extratoDeTransacoes(contaAutenticada.toString());

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

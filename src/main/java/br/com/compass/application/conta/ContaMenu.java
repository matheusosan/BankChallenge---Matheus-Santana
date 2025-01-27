package br.com.compass.application.conta;

import br.com.compass.application.conta.services.ContaService;
import br.com.compass.domain.entities.Conta;
import br.com.compass.domain.entities.Transacao;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.UUID;

public class ContaMenu {
    private final ContaService contaService;
    private final Scanner scanner;

    public ContaMenu(ContaService contaService) {
        this.contaService = contaService;
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

            Conta.TipoConta tipoConta;
            switch (opcao) {
                case 1 -> tipoConta = Conta.TipoConta.CONTA_CORRENTE;
                case 2 -> tipoConta = Conta.TipoConta.CONTA_SALARIO;
                case 3 -> tipoConta = Conta.TipoConta.CONTA_POUPANCA;
                default -> throw new IllegalArgumentException("Opção inválida para tipo de conta.");
            }

            contaService.criarConta(nome, dataNascimento, cpf, senha, numeroTelefone, tipoConta);
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

            idConta = contaService.realizarLogin(senha, cpf);

            System.out.println("Login bem sucedido!");
        } catch (RuntimeException e) {
            System.out.println("Ocorreu um erro: " + e.getMessage());
        }
        return idConta;
    };

    public void verificarSaldo(UUID contaAutenticada) {
        try {
            BigDecimal balance = contaService.verificarSaldo(contaAutenticada.toString());
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
            List<Transacao> transacoes = contaService.extratoDeTransacoes(contaAutenticada.toString());

            System.out.println("Transações da conta:");
                for (Transacao transacao : transacoes) {
                    System.out.printf(
                            "Tipo: %s, Quantia: %s, Data: %s%n",
                            transacao.getTipoTransacao(),
                            transacao.getQuantia(),
                            transacao.getCriadoEm()
                    );
                }

        } catch (RuntimeException e) {
            System.out.println("Erro ao buscar transações: " + e.getMessage());
        }
    }

}

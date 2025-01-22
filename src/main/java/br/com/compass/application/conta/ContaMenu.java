package br.com.compass.application.conta;

import br.com.compass.application.conta.services.ContaService;
import br.com.compass.domain.entities.Conta;
import br.com.compass.domain.entities.Transacao;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

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

            contaService.criarConta(nome, dataNascimento, cpf, numeroTelefone, tipoConta);
        } catch (Exception e) {
            System.out.println("Erro ao criar conta: " + e.getMessage());
        }
    }

    public void verificarSaldo() {
        try {
            System.out.print("Digite o número da conta a verificar o saldo: ");
            String contaId = scanner.nextLine();
            contaService.verificarSaldo(contaId);
        }
        catch (Exception e) {
                System.out.println("Erro ao buscar transações: " + e.getMessage());
            }
        }

    public void iniciarConsultaTransacoes() {
        try {
            System.out.print("Digite o número da conta para listar as transações: ");
            String numeroConta = scanner.nextLine();

            List<Transacao> transacoes = contaService.extratoDeTransacoes(numeroConta);

            if (transacoes.isEmpty()) {
                System.out.println("Nenhuma transação encontrada para esta conta.");
            }

            System.out.println("Transações da conta:");
                for (Transacao transacao : transacoes) {
                    System.out.printf(
                            "Tipo: %s, Quantia: %s, Data: %s%n",
                            transacao.getTipoTransacao(),
                            transacao.getQuantia(),
                            transacao.getCriadoEm()
                    );
                }

        } catch (Exception e) {
            System.out.println("Erro ao buscar transações: " + e.getMessage());
        }
    }

}

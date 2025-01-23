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

            if (valorDeposito.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("O valor do depósito deve ser positivo.");
            }

            transacaoService.realizarDeposito(contaAutenticada.toString(), valorDeposito);

        } catch (Exception e) {
            System.out.println("Erro ao realizar depósito: " + e.getMessage());
        }
    }

    public void iniciarSaque(UUID contaAutenticada) {
        try {
            System.out.print("Digite o valor do saque: ");
            String valorInput = scanner.nextLine();
            BigDecimal valorSaque = new BigDecimal(valorInput);

            if (valorSaque.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("O valor do saque deve ser positivo.");
            }

            transacaoService.realizarSaque(contaAutenticada.toString(), valorSaque);

        } catch (Exception e) {
            System.err.println("Erro ao realizar saque: " + e.getMessage());
        }
    }

    public void iniciarTransferencia(UUID contaAutenticada) {
        try {
            System.out.print("Digite o número da conta de destino: ");
            String contaDestinoId = scanner.nextLine();

            System.out.print("Digite o valor da transferência: ");
            String valorInput = scanner.nextLine();
            BigDecimal montante = new BigDecimal(valorInput);

            if (montante.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("O valor da transferência deve ser positivo.");
            }

            transacaoService.realizarTransferencia(contaDestinoId, contaAutenticada.toString(), montante);

        } catch (IllegalArgumentException e) {
            System.out.println("Erro ao realizar transferência: " + e.getMessage());
        }
    }


}
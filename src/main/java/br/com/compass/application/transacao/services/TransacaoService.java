package br.com.compass.application.transacao.services;

import br.com.compass.application.conta.repository.AccountRepository;
import br.com.compass.application.transacao.repository.TransactionRepository;
import br.com.compass.domain.entities.Conta;
import br.com.compass.domain.entities.Transacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class TransacaoService {
    private AccountRepository accountRepository = new AccountRepository();
    private TransactionRepository transactionRepository = new TransactionRepository();

    public void realizarDeposito(String contaId, BigDecimal montante) {

        try {

        Conta account = accountRepository.findById(UUID.fromString(contaId));

        if (account == null) {
            throw new IllegalArgumentException("Conta não encontrada.");
        }

        account.setSaldo(account.getSaldo().add(montante));

        accountRepository.update(account);

        Transacao transacao = new Transacao();
        transacao.setTipoTransacao(Transacao.TipoTransacao.DEPOSITO);
        transacao.setQuantia(montante);
        transacao.setCriadoEm(LocalDateTime.now());
        transacao.setConta(account);

        transactionRepository.save(transacao);
        System.out.println("Depósito realizado com sucesso!");
        }
        catch (Exception e) {
            System.out.println("Ocorreu um erro: " + e.getMessage());
        }
    }

    public void realizarSaque(String contaId, BigDecimal montante) {
        try {

        Conta account = accountRepository.findById(UUID.fromString(contaId));

        if (account == null) {
            throw new IllegalArgumentException("Conta não encontrada.");
        }

        if (account.getSaldo().compareTo(montante) < 0) {
            throw new RuntimeException("Saldo insuficiente para saque");
        }
        account.setSaldo(account.getSaldo().subtract(montante));

        Transacao transacao = new Transacao();
        transacao.setTipoTransacao(Transacao.TipoTransacao.SAQUE);
        transacao.setQuantia(montante);
        transacao.setCriadoEm(LocalDateTime.now());
        transacao.setConta(account);

        accountRepository.update(account);
        transactionRepository.save(transacao);

        System.out.println("Saque realizado com sucesso!");

    } catch (Exception e) {
        e.printStackTrace();
    }
}

    public void realizarTransferencia(String contaDestinoId, String contaBaseId, BigDecimal montante) {
        try {
            Conta senderAccount = accountRepository.findById(UUID.fromString(contaBaseId));
            Conta receiverAccount = accountRepository.findById(UUID.fromString(contaDestinoId));

            if (senderAccount == null) {
                throw new IllegalArgumentException("Conta base não encontrada.");
            }

            if (receiverAccount == null) {
                throw new IllegalArgumentException("Conta destino não encontrada.");
            }

            if (senderAccount.getSaldo().compareTo(montante) < 0) {
                throw new IllegalArgumentException("Saldo insuficiente na conta base para realizar a transferência.");
            }

            senderAccount.setSaldo(senderAccount.getSaldo().subtract(montante));
            receiverAccount.setSaldo(receiverAccount.getSaldo().add(montante));

            Transacao transacao = new Transacao();
            transacao.setTipoTransacao(Transacao.TipoTransacao.TRANSFERENCIA);
            transacao.setQuantia(montante);
            transacao.setCriadoEm(LocalDateTime.now());
            transacao.setConta(senderAccount);
            transacao.setContaDestino(receiverAccount);

            transactionRepository.save(transacao);
            accountRepository.update(senderAccount);
            accountRepository.update(receiverAccount);

            System.out.println("Transferência realizada com sucesso!");

        }  catch (Exception e) {
            e.printStackTrace();
        }
    }
}

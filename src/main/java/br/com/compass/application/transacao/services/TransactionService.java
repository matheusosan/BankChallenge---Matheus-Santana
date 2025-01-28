package br.com.compass.application.transacao.services;

import br.com.compass.application.conta.repository.AccountRepository;
import br.com.compass.application.transacao.repository.TransactionRepository;
import br.com.compass.domain.entities.Account;
import br.com.compass.domain.entities.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class TransactionService {
    private AccountRepository accountRepository = new AccountRepository();
    private TransactionRepository transactionRepository = new TransactionRepository();

    public void realizarDeposito(String contaId, BigDecimal montante) {
        if (montante.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor do depósito deve ser positivo.");
        }

        Account account = accountRepository.findById(UUID.fromString(contaId));

        if (account == null) {
            throw new IllegalArgumentException("Conta não encontrada.");
        }

        account.setBalance(account.getBalance().add(montante));

        accountRepository.update(account);

        Transaction transaction = new Transaction();
        transaction.setTransactionType(Transaction.TransactionType.DEPOSITO);
        transaction.setAmount(montante);
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setAccount(account);

        transactionRepository.save(transaction);

    }

    public void realizarSaque(String contaId, BigDecimal montante) {
        if (montante.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("O valor do saque deve ser positivo.");
        }

        Account account = accountRepository.findById(UUID.fromString(contaId));

        if (account == null) {
            throw new RuntimeException("Conta não encontrada.");
        }

        if (account.getBalance().compareTo(montante) < 0) {
            throw new RuntimeException("Saldo insuficiente para saque");
        }
        account.setBalance(account.getBalance().subtract(montante));

        Transaction transacao = new Transaction();
        transacao.setTransactionType(Transaction.TransactionType.SAQUE);
        transacao.setAmount(montante);
        transacao.setCreatedAt(LocalDateTime.now());
        transacao.setAccount(account);

        accountRepository.update(account);
        transactionRepository.save(transacao);
}

    public void realizarTransferencia(String contaDestinoId, String contaBaseId, BigDecimal montante) {
        if (montante.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor da transferência deve ser positivo.");
        }

            Account senderAccount = accountRepository.findById(UUID.fromString(contaBaseId));
            Account receiverAccount = accountRepository.findById(UUID.fromString(contaDestinoId));

            if (senderAccount == null) {
                throw new IllegalArgumentException("Conta base não encontrada.");
            }

            if (receiverAccount == null) {
                throw new IllegalArgumentException("Conta destino não encontrada.");
            }

            if (senderAccount.getBalance().compareTo(montante) < 0) {
                throw new IllegalArgumentException("Saldo insuficiente na conta base para realizar a transferência.");
            }

            senderAccount.setBalance(senderAccount.getBalance().subtract(montante));
            receiverAccount.setBalance(receiverAccount.getBalance().add(montante));

            Transaction transaction = new Transaction();
            transaction.setTransactionType(Transaction.TransactionType.TRANSFERENCIA);
            transaction.setAmount(montante);
            transaction.setCreatedAt(LocalDateTime.now());
            transaction.setAccount(senderAccount);
            transaction.setDestinyAccount(receiverAccount);

            transactionRepository.save(transaction);
            accountRepository.update(senderAccount);
            accountRepository.update(receiverAccount);
    }
}

package br.com.compass.application.transacao.services;

import br.com.compass.application.account.repository.AccountRepository;
import br.com.compass.application.transacao.repository.TransactionRepository;
import br.com.compass.domain.entities.Account;
import br.com.compass.domain.entities.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class TransactionService {
    private AccountRepository accountRepository = new AccountRepository();
    private TransactionRepository transactionRepository = new TransactionRepository();


    public void makeDeposit(String accountId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor do depósito deve ser positivo.");
        }

        Account account = accountRepository.findById(UUID.fromString(accountId));

        if (account == null) {
            throw new IllegalArgumentException("Conta não encontrada.");
        }

        account.setBalance(account.getBalance().add(amount));

        accountRepository.update(account);

        Transaction transaction = new Transaction();
        transaction.setTransactionType(Transaction.TransactionType.DEPOSITO);
        transaction.setAmount(amount);
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setAccount(account);

        transactionRepository.save(transaction);

    }

    public void makeWithdraw(String accountId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("O valor do saque deve ser positivo.");
        }

        Account account = accountRepository.findById(UUID.fromString(accountId));

        if (account == null) {
            throw new RuntimeException("Conta não encontrada.");
        }

        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Saldo insuficiente para saque");
        }
        account.setBalance(account.getBalance().subtract(amount));

        Transaction transaction = new Transaction();
        transaction.setTransactionType(Transaction.TransactionType.SAQUE);
        transaction.setAmount(amount);
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setAccount(account);

        accountRepository.update(account);
        transactionRepository.save(transaction);
}

    public void makeTransfer(String destinyAccountId, String baseAccountId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor da transferência deve ser positivo.");
        }

            Account senderAccount = accountRepository.findById(UUID.fromString(baseAccountId));
            Account receiverAccount = accountRepository.findById(UUID.fromString(destinyAccountId));

            if (senderAccount == null) {
                throw new IllegalArgumentException("Conta base não encontrada.");
            }

            if (receiverAccount == null) {
                throw new IllegalArgumentException("Conta destino não encontrada.");
            }

            if (senderAccount.getBalance().compareTo(amount) < 0) {
                throw new IllegalArgumentException("Saldo insuficiente na conta base para realizar a transferência.");
            }

            senderAccount.setBalance(senderAccount.getBalance().subtract(amount));
            receiverAccount.setBalance(receiverAccount.getBalance().add(amount));

            Transaction transaction = new Transaction();
            transaction.setTransactionType(Transaction.TransactionType.TRANSFERENCIA);
            transaction.setAmount(amount);
            transaction.setCreatedAt(LocalDateTime.now());
            transaction.setAccount(senderAccount);
            transaction.setDestinyAccount(receiverAccount);

            transactionRepository.save(transaction);
            accountRepository.update(senderAccount);
            accountRepository.update(receiverAccount);
    }
}

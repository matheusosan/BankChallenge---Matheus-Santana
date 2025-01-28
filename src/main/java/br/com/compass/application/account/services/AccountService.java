package br.com.compass.application.account.services;

import br.com.compass.application.account.repository.AccountRepository;
import br.com.compass.application.security.IEncryptionService;
import br.com.compass.application.transacao.repository.TransactionRepository;
import br.com.compass.application.user.repository.UserRepository;
import br.com.compass.domain.entities.Account;
import br.com.compass.domain.entities.Transaction;
import br.com.compass.domain.entities.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AccountService {
    private IEncryptionService encryptionService;
    private AccountRepository accountRepository = new AccountRepository();
    private TransactionRepository transactionRepository = new TransactionRepository();
    private UserRepository userRepository = new UserRepository();

    public AccountService(IEncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    public void createAccount(String name, LocalDate birthDate, String cpf, String password, String phoneNumber, Account.AccountType accountType) {
        Optional<Account> accountExists = accountRepository.findByCpf(cpf);

        if (accountExists.isPresent()) {
            throw new IllegalArgumentException("Já existe uma conta com o CPF fornecido.");
        }

        String hashedPassword = encryptionService.encryptPassword(password);


        User newUser = new User();
        newUser.setPhoneNumber(phoneNumber);
        newUser.setName(name);
        newUser.setCpf(cpf);
        newUser.setBirthDate(birthDate);

        userRepository.save(newUser);

        Account newAccount = new Account();
        newAccount.setPassword(hashedPassword);
        newAccount.setAccountType(accountType);
        newAccount.setLogin(cpf);
        newAccount.setBalance(BigDecimal.ZERO);
        newAccount.setUser(newUser);

        accountRepository.save(newAccount);
    }

    public UUID login(String password, String cpf) {
        Optional<Account> accountExists = accountRepository.findByCpf(cpf);

        if (accountExists.isEmpty()) {
            throw new RuntimeException("Account não encontrada.");
        }

        Account account = accountExists.get();

        boolean passwordMatches = encryptionService.verifyPassword(password, account.getPassword());

        if (!passwordMatches) {
            throw new RuntimeException("Senha incorreta.");
        }

        return account.getId();
    }

    public BigDecimal checkBalance(String accountId) {
        Account account = accountRepository.findById(UUID.fromString(accountId));

        if (account == null) {
            throw new RuntimeException("Account não encontrada.");
        }

        return account.getBalance();
    }

    public List<Transaction> checkStatement(String accountId) {
       List<Transaction> transactions = transactionRepository.findAllById(UUID.fromString(accountId));

       if(transactions.isEmpty()) {
           throw new RuntimeException("Você não possui transações.");
       }

       return transactions;
    };


}

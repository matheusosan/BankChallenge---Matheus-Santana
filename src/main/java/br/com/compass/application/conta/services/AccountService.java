package br.com.compass.application.conta.services;

import br.com.compass.application.conta.repository.AccountRepository;
import br.com.compass.application.security.ICriptografiaService;
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
    private ICriptografiaService criptografiaService;
    private AccountRepository accountRepository = new AccountRepository();
    private TransactionRepository transactionRepository = new TransactionRepository();
    private UserRepository userRepository = new UserRepository();

    public AccountService(ICriptografiaService criptografiaService) {
        this.criptografiaService = criptografiaService;
    }

    public void criarConta(String nome, LocalDate dataNascimento, String cpf, String senha, String numeroTelefone, Account.AccountType tipoAccount) {
        Optional<Account> accountExists = accountRepository.findByCpf(cpf);

        if (accountExists.isPresent()) {
            throw new IllegalArgumentException("Já existe uma conta com o CPF fornecido.");
        }

        String hashedPassword = criptografiaService.criptografarSenha(senha);


        User newUser = new User();
        newUser.setPhoneNumber(numeroTelefone);
        newUser.setName(nome);
        newUser.setCpf(cpf);
        newUser.setBirthDate(dataNascimento);

        userRepository.save(newUser);

        Account newAccount = new Account();
        newAccount.setPassword(hashedPassword);
        newAccount.setAccountType(tipoAccount);
        newAccount.setLogin(cpf);
        newAccount.setBalance(BigDecimal.ZERO);
        newAccount.setUser(newUser);

        accountRepository.save(newAccount);
    }

    public UUID realizarLogin(String senha, String cpf) {
        Optional<Account> accountExists = accountRepository.findByCpf(cpf);

        if (accountExists.isEmpty()) {
            throw new RuntimeException("Account não encontrada.");
        }

        Account account = accountExists.get();

        boolean passwordMatches = criptografiaService.verificarSenha(senha, account.getPassword());

        if (!passwordMatches) {
            throw new RuntimeException("Senha incorreta.");
        }

        return account.getId();
    }

    public BigDecimal verificarSaldo(String contaId) {
        Account account = accountRepository.findById(UUID.fromString(contaId));

        if (account == null) {
            throw new RuntimeException("Account não encontrada.");
        }

        return account.getBalance();
    }

    public List<Transaction> extratoDeTransacoes(String contaId) {
       List<Transaction> transactions = transactionRepository.findAllById(UUID.fromString(contaId));

       if(transactions.isEmpty()) {
           throw new RuntimeException("Você não possui transações.");
       }

       return transactions;
    };


}

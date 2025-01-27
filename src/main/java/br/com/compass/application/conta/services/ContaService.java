package br.com.compass.application.conta.services;

import br.com.compass.application.conta.repository.AccountRepository;
import br.com.compass.application.security.ICriptografiaService;
import br.com.compass.application.transacao.repository.TransactionRepository;
import br.com.compass.domain.entities.Conta;
import br.com.compass.domain.entities.Transacao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ContaService {
    private ICriptografiaService criptografiaService;
    private AccountRepository accountRepository = new AccountRepository();
    private TransactionRepository transactionRepository = new TransactionRepository();

    public ContaService(ICriptografiaService criptografiaService) {
        this.criptografiaService = criptografiaService;
    }

    public void criarConta(String nome, LocalDate dataNascimento, String cpf, String senha, String numeroTelefone, Conta.TipoConta tipoConta) {
        Optional<Conta> accountExists = accountRepository.findByCpf(cpf);

        if (accountExists.isPresent()) {
            throw new IllegalArgumentException("Já existe uma conta com o CPF fornecido.");
        }

        String hashedPassword = criptografiaService.criptografarSenha(senha);

        Conta newAccount = new Conta();
        newAccount.setTipoConta(tipoConta);
        newAccount.setCpf(cpf);
        newAccount.setSenha(hashedPassword);
        newAccount.setDataNascimento(dataNascimento);
        newAccount.setSaldo(BigDecimal.ZERO);
        newAccount.setNome(nome);
        newAccount.setNumeroTelefone(numeroTelefone);

        accountRepository.save(newAccount);
    }

    public UUID realizarLogin(String senha, String cpf) {
        Optional<Conta> accountExists = accountRepository.findByCpf(cpf);

        if (accountExists.isEmpty()) {
            throw new RuntimeException("Conta não encontrada.");
        }

        Conta account = accountExists.get();

        boolean passwordMatches = criptografiaService.verificarSenha(senha, account.getSenha());

        if (!passwordMatches) {
            throw new RuntimeException("Senha incorreta.");
        }

        return account.getId();
    }

    public BigDecimal verificarSaldo(String contaId) {
        Conta account = accountRepository.findById(UUID.fromString(contaId));

        if (account == null) {
            throw new RuntimeException("Conta não encontrada.");
        }

        return account.getSaldo();
    }

    public List<Transacao> extratoDeTransacoes(String contaId) {
       List<Transacao> transactions = transactionRepository.findAllById(UUID.fromString(contaId));

       if(transactions.isEmpty()) {
           throw new RuntimeException("Você não possui transações.");
       }

       return transactions;
    };


}

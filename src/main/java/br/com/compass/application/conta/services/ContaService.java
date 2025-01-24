package br.com.compass.application.conta.services;

import br.com.compass.application.conta.repository.AccountRepository;
import br.com.compass.application.security.ICriptografiaService;
import br.com.compass.application.transacao.repository.TransactionRepository;
import br.com.compass.domain.entities.Conta;
import br.com.compass.domain.entities.Transacao;
import br.com.compass.infra.config.HibernateConfig;
import org.hibernate.Session;
import org.hibernate.query.Query;

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

        System.out.println("Conta salva com sucesso!");
    }

    public UUID realizarLogin(String senha, String cpf) {
        Optional<Conta> accountExists = accountRepository.findByCpf(cpf);

        if (accountExists.isEmpty()) {
            System.out.println("Conta não encontrada.");
            return null;
        }

        Conta account = accountExists.get();

        boolean passwordMatches = criptografiaService.verificarSenha(senha, account.getSenha());

        if (!passwordMatches) {
            System.out.println("Senha incorreta.");
            return null;
        }

        System.out.println("Login bem-sucedido!");
        return account.getId();
    }

    public BigDecimal verificarSaldo(String contaId) {
        Conta account = accountRepository.findById(UUID.fromString(contaId));

        if (account == null) {
            System.out.println("Conta não encontrada.");
            return null;
        }

        return account.getSaldo();
    }

    public List<Transacao> extratoDeTransacoes(String contaId) {
       return transactionRepository.findAllById(UUID.fromString(contaId));
    };


}

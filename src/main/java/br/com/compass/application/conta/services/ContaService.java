package br.com.compass.application.conta.services;

import br.com.compass.domain.entities.Conta;
import br.com.compass.domain.entities.Transacao;
import br.com.compass.infra.config.HibernateConfig;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class ContaService {

    public void criarConta(String nome, LocalDate dataNascimento, String cpf, String numeroTelefone, Conta.TipoConta tipoConta) {
        Session session = HibernateConfig.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "FROM Conta c WHERE c.cpf = :cpf";
        Query<Conta> query = session.createQuery(hql, Conta.class);
        query.setParameter("cpf", cpf);

        List<Conta> contasExistentes = query.getResultList();

        if (!contasExistentes.isEmpty()) {
            throw new IllegalArgumentException("Já existe uma conta com o mesmo CPF ou número de conta.");
        }

        try {
            Conta novaConta = new Conta();
            novaConta.setTipoConta(tipoConta);
            novaConta.setCpf(cpf);
            novaConta.setDataNascimento(dataNascimento);
            novaConta.setSaldo(BigDecimal.ZERO);
            novaConta.setNome(nome);
            novaConta.setNumeroTelefone(numeroTelefone);

            session.persist(novaConta);

            transaction.commit();

            System.out.println("Conta salva com sucesso!");
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Erro ao criar conta: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public void verificarSaldo(String contaId) {
        Session session = HibernateConfig.getSessionFactory().openSession();

        try {
            String hql = "SELECT c.saldo FROM Conta c WHERE c.id = :contaId";
            Query<BigDecimal> query = session.createQuery(hql, BigDecimal.class);
            query.setParameter("contaId", UUID.fromString(contaId));

            BigDecimal saldo = query.uniqueResult();

            if (saldo == null) {
                throw new IllegalArgumentException("Conta não encontrada com o ID fornecido.");
            }

            System.out.println(saldo);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar saldo da conta: " + e.getMessage());
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public List<Transacao> extratoDeTransacoes(String contaId) {
        Session session = HibernateConfig.getSessionFactory().openSession();

        try {
            String hql = "FROM Transacao t WHERE t.conta.id = :contaId ORDER BY t.criadoEm DESC";
            Query<Transacao> query = session.createQuery(hql, Transacao.class);
            query.setParameter("contaId", UUID.fromString(contaId));
            return query.getResultList();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar transações para a conta: " + e.getMessage());
        } finally {
            if (session != null) {
                session.close();
            }
        }
    };


}

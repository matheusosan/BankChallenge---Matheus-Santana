package br.com.compass.application.conta.repository;

import br.com.compass.domain.entities.Account;
import br.com.compass.infra.config.HibernateConfig;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AccountRepository {

    public Account findById(UUID contaId) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            return session.find(Account.class, contaId);
        }
    }

    public Optional<Account> findByCpf(String cpf) {
        Session session = HibernateConfig.getSessionFactory().openSession();
        String hql = "FROM Account c WHERE c.login = :cpf";

        List<Account> accounts = session.createQuery(hql, Account.class)
                .setParameter("cpf", cpf)
                .getResultList();
        session.close();

        if (accounts.size() > 1) {
            throw new IllegalStateException("Mais de uma conta encontrada com o mesmo CPF.");
        }

        return accounts.isEmpty() ? Optional.empty() : Optional.of(accounts.get(0));
    }

    public void save(Account conta) {
        Session session = HibernateConfig.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.persist(conta);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }

    public void update(Account account) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.merge(account);
            session.getTransaction().commit();
        }

    }
}

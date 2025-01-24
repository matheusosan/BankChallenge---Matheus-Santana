package br.com.compass.application.conta.repository;

import br.com.compass.domain.entities.Conta;
import br.com.compass.infra.config.HibernateConfig;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AccountRepository {

    public Conta findById(UUID contaId) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            return session.find(Conta.class, contaId);
        }
    }

    public Optional<Conta> findByCpf(String cpf) {
        Session session = HibernateConfig.getSessionFactory().openSession();
        String hql = "FROM Conta c WHERE c.cpf = :cpf";

        List<Conta> accounts = session.createQuery(hql, Conta.class)
                .setParameter("cpf", cpf)
                .getResultList();
        session.close();

        if (accounts.size() > 1) {
            throw new IllegalStateException("Mais de uma conta encontrada com o mesmo CPF.");
        }

        return accounts.isEmpty() ? Optional.empty() : Optional.of(accounts.get(0));
    }

    public void save(Conta conta) {
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

    public void update(Conta account) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.merge(account);
            session.getTransaction().commit();
        }

    }
}

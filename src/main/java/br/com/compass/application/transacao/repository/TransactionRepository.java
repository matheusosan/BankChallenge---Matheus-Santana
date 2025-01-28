package br.com.compass.application.transacao.repository;

import br.com.compass.domain.entities.Transaction;
import br.com.compass.infra.config.HibernateConfig;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.UUID;

public class TransactionRepository {

    public void save(Transaction transacao) {
        Session session = HibernateConfig.getSessionFactory().openSession();
        org.hibernate.Transaction transaction = session.beginTransaction();
        try {
            session.persist(transacao);
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

    public List<Transaction> findAllById(UUID contaId) {
        Session session = HibernateConfig.getSessionFactory().openSession();
        org.hibernate.Transaction transaction = session.beginTransaction();
        try {
            String hql = "FROM Transaction t WHERE t.account.id = :contaId ORDER BY t.createdAt DESC";
            Query<Transaction> query = session.createQuery(hql, Transaction.class);
            query.setParameter("contaId", contaId);
            return query.getResultList();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }
}

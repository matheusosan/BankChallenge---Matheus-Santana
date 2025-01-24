package br.com.compass.application.transacao.repository;

import br.com.compass.domain.entities.Transacao;
import br.com.compass.infra.config.HibernateConfig;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.UUID;

public class TransactionRepository {

    public void save(Transacao transacao) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.persist(transacao);
            session.getTransaction().commit();
        }
    }

    public List<Transacao> findAllById(UUID contaId) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            String hql = "FROM Transacao t WHERE t.conta.id = :contaId ORDER BY t.criadoEm DESC";
            Query<Transacao> query = session.createQuery(hql, Transacao.class);
            query.setParameter("contaId", contaId);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar transações para a conta: " + e.getMessage());
        }
    }
}

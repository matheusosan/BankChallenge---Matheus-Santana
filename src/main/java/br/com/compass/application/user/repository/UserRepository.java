package br.com.compass.application.user.repository;

import br.com.compass.domain.entities.User;
import br.com.compass.infra.config.HibernateConfig;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class UserRepository {

    public void save(User user) {
        Session session = HibernateConfig.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.persist(user);
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
}

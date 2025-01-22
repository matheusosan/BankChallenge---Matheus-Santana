package br.com.compass.application.transacao.services;

import br.com.compass.domain.entities.Conta;
import br.com.compass.domain.entities.Transacao;
import br.com.compass.infra.config.HibernateConfig;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class TransacaoService {


    public void realizarDeposito(String contaId, BigDecimal montante) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            UUID id = UUID.fromString(contaId);

            Transaction transaction = session.beginTransaction();

            Conta conta = session.find(Conta.class, id);

            if (conta == null) {
                throw new IllegalArgumentException("Conta não encontrada.");
            }

            conta.setSaldo(conta.getSaldo().add(montante));

            Transacao transacao = new Transacao();
            transacao.setTipoTransacao(Transacao.TipoTransacao.DEPOSITO);
            transacao.setQuantia(montante);
            transacao.setCriadoEm(LocalDateTime.now());
            transacao.setConta(conta);

            session.persist(transacao);

            session.merge(conta);

            transaction.commit();

            System.out.println("Depósito realizado com sucesso!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void realizarSaque(String contaId, BigDecimal montante) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            Conta conta = session.find(Conta.class, UUID.fromString(contaId));

            if (conta == null) {
                throw new IllegalArgumentException("Conta não encontrada.");
            }

            if (conta.getSaldo().compareTo(montante) < 0) {
                throw new RuntimeException("Saldo insuficiente para saque");
            }

            conta.setSaldo(conta.getSaldo().subtract(montante));

            Transacao transacao = new Transacao();
            transacao.setTipoTransacao(Transacao.TipoTransacao.SAQUE);
            transacao.setQuantia(montante);
            transacao.setCriadoEm(LocalDateTime.now());
            transacao.setConta(conta);

            session.persist(transacao);

            session.merge(conta);

            transaction.commit();

            System.out.println("Saque realizado com sucesso!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void realizarTransferencia(String contaDestinoId, String contaBaseId, BigDecimal montante) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {

            Transaction transaction = session.beginTransaction();

            Conta contaBase = session.find(Conta.class, UUID.fromString(contaBaseId));
            Conta contaDestino = session.find(Conta.class, UUID.fromString(contaDestinoId));

            if (contaBase == null) {
                throw new IllegalArgumentException("Conta base não encontrada.");
            }

            if (contaDestino == null) {
                throw new IllegalArgumentException("Conta destino não encontrada.");
            }

            if (contaBase.getSaldo().compareTo(montante) < 0) {
                throw new IllegalArgumentException("Saldo insuficiente na conta base para realizar a transferência.");
            }

            contaBase.setSaldo(contaBase.getSaldo().subtract(montante));
            contaDestino.setSaldo(contaDestino.getSaldo().add(montante));

            Transacao transacao = new Transacao();
            transacao.setTipoTransacao(Transacao.TipoTransacao.TRANSFERENCIA);
            transacao.setQuantia(montante);
            transacao.setCriadoEm(LocalDateTime.now());
            transacao.setConta(contaBase);
            transacao.setContaDestino(contaDestino);

            session.persist(transacao);

            session.merge(contaBase);
            session.merge(contaDestino);

            transaction.commit();

            System.out.println("Transferência realizada com sucesso!");
        }  catch (Exception e) {
            e.printStackTrace();
        }
    }
}

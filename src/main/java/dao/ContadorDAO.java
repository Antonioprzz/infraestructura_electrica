package dao;

import model.Contador;
import util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import java.util.Optional;

public class ContadorDAO {

    public Contador guardar(Contador contador) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(contador);
            tx.commit();
            return contador;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public Optional<Contador> buscarPorId(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return Optional.ofNullable(em.find(Contador.class, id));
        } finally {
            em.close();
        }
    }

    public Optional<Contador> buscarPorContrato(Long contratoId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Contador c = em.createQuery(
                            "SELECT c FROM Contador c WHERE c.contrato.id = :contratoId", Contador.class)
                    .setParameter("contratoId", contratoId)
                    .getSingleResult();
            return Optional.of(c);
        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }

    public boolean existeConNumeroSerie(String numeroSerie) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(c) FROM Contador c WHERE c.numeroSerie = :ns", Long.class)
                    .setParameter("ns", numeroSerie)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    public Contador actualizar(Contador contador) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Contador actualizado = em.merge(contador);
            tx.commit();
            return actualizado;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public boolean eliminar(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Contador c = em.find(Contador.class, id);
            if (c == null) { tx.rollback(); return false; }
            em.remove(c);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}

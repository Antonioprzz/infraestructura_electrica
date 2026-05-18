package dao;

import model.Titular;
import util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;

public class TitularDAO {

    public Titular guardar(Titular titular) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(titular);
            tx.commit();
            return titular;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public Optional<Titular> buscarPorId(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return Optional.ofNullable(em.find(Titular.class, id));
        } finally {
            em.close();
        }
    }

    public List<Titular> listarTodos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT t FROM Titular t ORDER BY t.nombreCompleto", Titular.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public boolean existeConNif(String nif) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(t) FROM Titular t WHERE t.nif = :nif", Long.class)
                    .setParameter("nif", nif)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    public Titular actualizar(Titular titular) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Titular actualizado = em.merge(titular);
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
            Titular t = em.find(Titular.class, id);
            if (t == null) { tx.rollback(); return false; }
            em.remove(t);
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

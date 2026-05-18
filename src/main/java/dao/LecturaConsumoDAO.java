package dao;

import model.LecturaConsumo;
import util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;

public class LecturaConsumoDAO {

    public LecturaConsumo guardar(LecturaConsumo lectura) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(lectura);
            tx.commit();
            return lectura;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public Optional<LecturaConsumo> buscarPorId(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return Optional.ofNullable(em.find(LecturaConsumo.class, id));
        } finally {
            em.close();
        }
    }

    public List<LecturaConsumo> listarPorContador(Long contadorId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT l FROM LecturaConsumo l WHERE l.contador.id = :contadorId " +
                                    "ORDER BY l.fechaHora DESC", LecturaConsumo.class)
                    .setParameter("contadorId", contadorId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public LecturaConsumo actualizar(LecturaConsumo lectura) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            LecturaConsumo actualizada = em.merge(lectura);
            tx.commit();
            return actualizada;
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
            LecturaConsumo l = em.find(LecturaConsumo.class, id);
            if (l == null) { tx.rollback(); return false; }
            em.remove(l);
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

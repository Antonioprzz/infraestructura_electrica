package dao;

import model.Subestacion;
import util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

public class SubestacionDAO {

    public Subestacion guardar(Subestacion subestacion) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(subestacion);
            tx.commit();
            return subestacion;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public Optional<Subestacion> buscarPorId(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Subestacion s = em.find(Subestacion.class, id);
            return Optional.ofNullable(s);
        } finally {
            em.close();
        }
    }

    public List<Subestacion> listarTodas() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT s FROM Subestacion s ORDER BY s.nombre", Subestacion.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public boolean existeConNombre(String nombre) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(s) FROM Subestacion s WHERE s.nombre = :nombre", Long.class)
                    .setParameter("nombre", nombre)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    public List<Subestacion> listarConectadas(Long subestacionId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            // Subestaciones destino cuando la subestación dada es origen
            List<Subestacion> resultado = em.createQuery(
                            "SELECT l.subestacionDestino FROM LineaTransporte l " +
                                    "WHERE l.subestacionOrigen.id = :id", Subestacion.class)
                    .setParameter("id", subestacionId)
                    .getResultList();

            // Subestaciones origen cuando la subestación dada es destino
            List<Subestacion> comoDestino = em.createQuery(
                            "SELECT l.subestacionOrigen FROM LineaTransporte l " +
                                    "WHERE l.subestacionDestino.id = :id", Subestacion.class)
                    .setParameter("id", subestacionId)
                    .getResultList();

            resultado.addAll(comoDestino);
            return resultado;
        } finally {
            em.close();
        }
    }

    public Subestacion actualizar(Subestacion subestacion) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Subestacion actualizada = em.merge(subestacion);
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
            Subestacion s = em.find(Subestacion.class, id);
            if (s == null) {
                tx.rollback();
                return false;
            }
            em.remove(s);
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

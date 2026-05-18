package dao;

import model.LineaTransporte;
import util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;

public class LineaTransporteDAO {

    public LineaTransporte guardar(LineaTransporte linea) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(linea);
            tx.commit();
            return linea;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public Optional<LineaTransporte> buscarPorId(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return Optional.ofNullable(em.find(LineaTransporte.class, id));
        } finally {
            em.close();
        }
    }

    public List<LineaTransporte> listarTodas() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT l FROM LineaTransporte l ORDER BY l.codigo", LineaTransporte.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public boolean existeConCodigo(String codigo) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(l) FROM LineaTransporte l WHERE l.codigo = :codigo", Long.class)
                    .setParameter("codigo", codigo)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    public LineaTransporte actualizar(LineaTransporte linea) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            LineaTransporte actualizada = em.merge(linea);
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
            LineaTransporte l = em.find(LineaTransporte.class, id);
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
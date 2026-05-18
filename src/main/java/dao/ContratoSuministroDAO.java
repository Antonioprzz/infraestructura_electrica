package dao;

import model.ContratoSuministro;
import util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;

public class ContratoSuministroDAO {

    public ContratoSuministro guardar(ContratoSuministro contrato) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(contrato);
            tx.commit();
            return contrato;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public Optional<ContratoSuministro> buscarPorId(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return Optional.ofNullable(em.find(ContratoSuministro.class, id));
        } finally {
            em.close();
        }
    }
    
    public List<ContratoSuministro> listarPorTitular(Long titularId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT c FROM ContratoSuministro c WHERE c.titular.id = :titularId " +
                                    "ORDER BY c.fechaAlta", ContratoSuministro.class)
                    .setParameter("titularId", titularId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public boolean existeConCodigo(String codigo) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(c) FROM ContratoSuministro c WHERE c.codigoContrato = :codigo", Long.class)
                    .setParameter("codigo", codigo)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    public ContratoSuministro actualizar(ContratoSuministro contrato) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            ContratoSuministro actualizado = em.merge(contrato);
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
            ContratoSuministro c = em.find(ContratoSuministro.class, id);
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

package dao;

import model.ContratoSuministro;
import util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;

/**
 * Clase DAO para los contratos de suministro.
 * Tiene los métodos para guardar, buscar, listar, actualizar y borrar
 * contratos en la base de datos usando JPA.
 */
public class ContratoSuministroDAO {

    /**
     * Guarda un contrato nuevo en la BBDD.
     *
     * @param contrato el contrato que queremos guardar.
     * @return el contrato ya guardado (ya con id asignado).
     */
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

    /**
     * Busca un contrato por su id.
     *
     * @param id el id del contrato.
     * @return un Optional con el contrato si existe, o vacío si no.
     */
    public Optional<ContratoSuministro> buscarPorId(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return Optional.ofNullable(em.find(ContratoSuministro.class, id));
        } finally {
            em.close();
        }
    }

    /**
     * Devuelve todos los contratos que tiene un titular,
     * ordenados por fecha de alta.
     *
     * @param titularId el id del titular.
     * @return lista con sus contratos (puede estar vacía).
     */
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

    /**
     * Comprueba si ya hay un contrato con ese código.
     * Sirve para no repetir códigos al dar de alta un contrato.
     *
     * @param codigo el código a comprobar.
     * @return true si ya existe, false si no.
     */
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

    /**
     * Actualiza los datos de un contrato que ya existe.
     *
     * @param contrato el contrato con los datos nuevos.
     * @return el contrato actualizado.
     */
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

    /**
     * Borra el contrato cuyo id se le pasa.
     *
     * @param id el id del contrato a borrar.
     * @return true si se borró, false si no se encontró.
     */
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

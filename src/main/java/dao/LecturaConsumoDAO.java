package dao;

import model.LecturaConsumo;
import util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;

/**
 * Clase DAO para las lecturas de consumo.
 * Permite guardar, buscar, listar, actualizar y borrar lecturas
 * en la base de datos a través de JPA.
 */
public class LecturaConsumoDAO {

    /**
     * Guarda una lectura nueva en la BBDD.
     *
     * @param lectura la lectura a guardar.
     * @return la lectura guardada (con id ya asignado).
     */
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

    /**
     * Busca una lectura por su id.
     *
     * @param id el id de la lectura.
     * @return un Optional con la lectura si existe, o vacío si no.
     */
    public Optional<LecturaConsumo> buscarPorId(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return Optional.ofNullable(em.find(LecturaConsumo.class, id));
        } finally {
            em.close();
        }
    }

    /**
     * Devuelve todas las lecturas de un contador, de la más nueva a la más vieja.
     *
     * @param contadorId el id del contador.
     * @return lista de lecturas (puede estar vacía).
     */
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

    /**
     * Actualiza los datos de una lectura existente.
     *
     * @param lectura la lectura con los nuevos datos.
     * @return la lectura ya actualizada.
     */
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

    /**
     * Borra la lectura cuyo id se le pasa.
     *
     * @param id el id de la lectura a borrar.
     * @return true si se borró, false si no se encontró.
     */
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

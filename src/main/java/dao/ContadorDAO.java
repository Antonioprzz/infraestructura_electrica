package dao;

import model.Contador;
import util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import java.util.Optional;

/**
 * Clase DAO para trabajar con la entidad Contador.
 * Aquí están los métodos para guardar, buscar, actualizar y borrar contadores
 * en la base de datos usando JPA.
 */
public class ContadorDAO {

    /**
     * Guarda un contador nuevo en la base de datos.
     *
     * @param contador el contador que queremos guardar.
     * @return el contador ya guardado.
     */
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

    /**
     * Busca un contador por su id.
     *
     * @param id el id del contador.
     * @return un Optional con el contador si existe, o vacío si no se encuentra.
     */
    public Optional<Contador> buscarPorId(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return Optional.ofNullable(em.find(Contador.class, id));
        } finally {
            em.close();
        }
    }

    /**
     * Busca el contador que pertenece a un contrato.
     *
     * @param contratoId el id del contrato.
     * @return un Optional con el contador del contrato, o vacío si no tiene.
     */
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

    /**
     * Comprueba si ya hay un contador con ese número de serie.
     * Sirve para no repetir números de serie al dar de alta uno nuevo.
     *
     * @param numeroSerie el número de serie a comprobar.
     * @return true si ya existe, false si no.
     */
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

    /**
     * Actualiza los datos de un contador que ya existe.
     *
     * @param contador el contador con los datos nuevos.
     * @return el contador ya actualizado.
     */
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

    /**
     * Borra el contador con el id que le pasemos.
     *
     * @param id el id del contador a borrar.
     * @return true si se ha borrado, false si no existía.
     */
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
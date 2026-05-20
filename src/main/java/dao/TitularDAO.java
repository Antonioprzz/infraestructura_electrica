package dao;

import model.Titular;
import util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;

/**
 * Clase DAO para los titulares.
 * Permite guardar, buscar, listar, actualizar y borrar titulares en la BBDD.
 */
public class TitularDAO {

    /**
     * Guarda un titular nuevo en la BBDD.
     *
     * @param titular el titular a guardar.
     * @return el titular guardado (con id ya asignado).
     */
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

    /**
     * Busca un titular por su id.
     *
     * @param id el id del titular.
     * @return un Optional con el titular si existe, vacío si no.
     */
    public Optional<Titular> buscarPorId(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return Optional.ofNullable(em.find(Titular.class, id));
        } finally {
            em.close();
        }
    }

    /**
     * Devuelve todos los titulares ordenados por nombre.
     *
     * @return lista con todos los titulares.
     */
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

    /**
     * Comprueba si ya existe un titular con ese NIF.
     *
     * @param nif el NIF a comprobar.
     * @return true si existe, false si no.
     */
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

    /**
     * Actualiza los datos de un titular existente.
     *
     * @param titular el titular con los datos nuevos.
     * @return el titular actualizado.
     */
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

    /**
     * Borra el titular cuyo id se le pasa.
     *
     * @param id el id del titular a borrar.
     * @return true si se borró, false si no se encontró.
     */
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

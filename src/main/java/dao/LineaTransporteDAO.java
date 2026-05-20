package dao;

import model.LineaTransporte;
import util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;

/**
 * Clase DAO para las líneas de transporte.
 * Permite guardar, buscar, listar, actualizar y borrar líneas de
 * transporte en la BBDD usando JPA.
 */
public class LineaTransporteDAO {

    /**
     * Guarda una línea nueva en la BBDD.
     *
     * @param linea la línea a guardar.
     * @return la línea guardada (ya con id).
     */
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

    /**
     * Busca una línea por su id.
     *
     * @param id el id de la línea.
     * @return un Optional con la línea si existe, vacío si no.
     */
    public Optional<LineaTransporte> buscarPorId(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return Optional.ofNullable(em.find(LineaTransporte.class, id));
        } finally {
            em.close();
        }
    }

    /**
     * Devuelve todas las líneas, ordenadas por código.
     *
     * @return lista con todas las líneas (puede estar vacía).
     */
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

    /**
     * Comprueba si ya existe una línea con ese código.
     *
     * @param codigo el código a comprobar.
     * @return true si ya existe, false si no.
     */
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

    /**
     * Actualiza los datos de una línea existente.
     *
     * @param linea la línea con los datos nuevos.
     * @return la línea actualizada.
     */
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

    /**
     * Borra la línea cuyo id se le pasa.
     *
     * @param id el id de la línea a borrar.
     * @return true si se borró, false si no se encontró.
     */
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
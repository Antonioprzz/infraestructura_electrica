package util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Clase de utilidad para gestionar JPA.
 * Se encarga de crear y cerrar el EntityManagerFactory que es el que
 * conecta con la base de datos. Así, todas las DAO usan esta clase
 * para conseguir un EntityManager y trabajar con la BBDD.
 *
 * Como tiene métodos estáticos y un constructor privado, no se puede
 * instanciar (es un patrón de "clase de utilidad").
 */
public class JPAUtil {

    /** Nombre de la unidad de persistencia (la que está en persistence.xml). */
    private static final String PERSISTENCE_UNIT = "redElectricaPU";

    /** Fábrica de EntityManager. Se crea una sola vez y se reutiliza. */
    private static EntityManagerFactory emf;

    /** Constructor privado para que nadie pueda crear objetos de esta clase. */
    private JPAUtil() {}

    /**
     * Inicializa la conexión con la base de datos.
     * Si ya está abierta, no hace nada.
     */
    public static void init() {
        if (emf == null || !emf.isOpen()) {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
        }
    }

    /**
     * Devuelve un EntityManager para hacer operaciones con la BBDD.
     * Si la fábrica no está creada todavía, la inicializa.
     *
     * @return un EntityManager nuevo.
     */
    public static EntityManager getEntityManager() {
        if (emf == null || !emf.isOpen()) {
            init();
        }
        return emf.createEntityManager();
    }

    /**
     * Cierra la conexión con la base de datos.
     * Se llama al salir del programa para liberar recursos.
     */
    public static void shutdown() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}

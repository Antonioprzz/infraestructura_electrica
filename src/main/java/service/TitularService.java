package service;

import dao.TitularDAO;
import model.Titular;
import util.ValidationException;

import java.util.List;
import java.util.Optional;

/**
 * Clase de servicio para los titulares.
 * Valida el formato del NIF y la unicidad antes de mandar los datos
 * a la DAO. Si algo no es correcto, lanza {@link ValidationException}.
 */
public class TitularService {

    /** DAO para acceder a los titulares en la BBDD. */
    private final TitularDAO dao = new TitularDAO();

    // ── Validación de DNI/NIF español ────────────────────────────────────────
    // Formato: 8 dígitos + 1 letra (cualquiera)
    /**
     * Comprueba que el NIF tenga el formato válido (8 dígitos + 1 letra).
     *
     * @param nif el NIF a validar.
     * @throws ValidationException si está vacío o tiene mal formato.
     */
    private static void validarNif(String nif) {
        if (nif == null || nif.isBlank())
            throw new ValidationException("El NIF es obligatorio.");

        String n = nif.trim().toUpperCase();

        // Formato: exactamente 8 dígitos seguidos de cualquier letra
        if (!n.matches("\\d{8}[A-Z]"))
            throw new ValidationException(
                    "El NIF '" + nif.trim() + "' no tiene un formato válido. " +
                            "Debe contener 8 dígitos seguidos de una letra (ej: 12345678Z).");
    }
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Registra un titular nuevo, validando nombre y NIF.
     *
     * @param nombreCompleto nombre o razón social (obligatorio).
     * @param nif NIF (obligatorio, único y con formato correcto).
     * @param direccion dirección (opcional).
     * @param email email (opcional).
     * @return el titular ya registrado.
     * @throws ValidationException si algún dato no es válido.
     */
    public Titular registrar(String nombreCompleto, String nif, String direccion, String email) {
        if (nombreCompleto == null || nombreCompleto.isBlank())
            throw new ValidationException("El nombre completo o razón social es obligatorio.");

        validarNif(nif);

        // RS-004
        if (dao.existeConNif(nif.trim()))
            throw new ValidationException(
                    "Ya existe un titular con el NIF '" + nif + "'. (RS-004)");

        return dao.guardar(new Titular(nombreCompleto.trim(), nif.trim().toUpperCase(),
                direccion, email));
    }

    /**
     * Busca un titular por id.
     *
     * @param id id del titular.
     * @return Optional con el titular si existe, vacío si no.
     */
    public Optional<Titular> buscarPorId(Long id) {
        return dao.buscarPorId(id);
    }

    /**
     * Devuelve todos los titulares.
     *
     * @return lista con todos los titulares.
     */
    public List<Titular> listarTodos() {
        return dao.listarTodos();
    }

    /**
     * Actualiza los datos de un titular.
     *
     * @param id id del titular a actualizar.
     * @param nombreCompleto nombre nuevo (obligatorio).
     * @param nif NIF nuevo (con formato válido).
     * @param direccion dirección nueva.
     * @param email email nuevo.
     * @return el titular actualizado.
     * @throws ValidationException si los datos no son válidos.
     */
    public Titular actualizar(Long id, String nombreCompleto, String nif,
                              String direccion, String email) {
        Titular t = dao.buscarPorId(id)
                .orElseThrow(() -> new ValidationException("No existe ningún titular con ID " + id + "."));

        if (nombreCompleto == null || nombreCompleto.isBlank())
            throw new ValidationException("El nombre es obligatorio.");

        validarNif(nif);

        // RS-004: unicidad, sólo si cambió
        if (!t.getNif().equalsIgnoreCase(nif.trim()) && dao.existeConNif(nif.trim()))
            throw new ValidationException("Ya existe un titular con el NIF '" + nif + "'. (RS-004)");

        t.setNombreCompleto(nombreCompleto.trim());
        t.setNif(nif.trim().toUpperCase());
        t.setDireccion(direccion);
        t.setEmail(email);
        return dao.actualizar(t);
    }

    /**
     * Borra un titular por id.
     *
     * @param id id del titular a borrar.
     * @return true si se borra correctamente.
     * @throws ValidationException si no existe.
     */
    public boolean eliminar(Long id) {
        if (!dao.buscarPorId(id).isPresent())
            throw new ValidationException("No existe ningún titular con ID " + id + ".");
        return dao.eliminar(id);
    }
}
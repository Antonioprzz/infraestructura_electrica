package service;

import dao.SubestacionDAO;
import model.Subestacion;
import util.ValidationException;

import java.util.List;
import java.util.Optional;

/**
 * Clase de servicio para las subestaciones.
 * Hace de "intermediario" entre el menú principal y la DAO:
 * comprueba que los datos sean válidos antes de mandarlos a la BBDD
 * y lanza {@link ValidationException} si algo no cuadra.
 */
public class SubestacionService {

    /** DAO que se usa para acceder a la BBDD de subestaciones. */
    private final SubestacionDAO dao = new SubestacionDAO();

    /**
     * Registra una subestación nueva, validando los datos.
     *
     * @param nombre nombre de la subestación (obligatorio y único).
     * @param provincia provincia (obligatoria).
     * @param latitud latitud.
     * @param longitud longitud.
     * @param capacidadMaximaMW capacidad máxima en MW (debe ser mayor que 0).
     * @return la subestación ya registrada.
     * @throws ValidationException si algún dato no es válido o el nombre se repite.
     */
    public Subestacion registrar(String nombre, String provincia, double latitud,
                                 double longitud, double capacidadMaximaMW) {
        // RS-001: campos obligatorios
        if (nombre == null || nombre.isBlank())
            throw new ValidationException("El nombre de la subestación es obligatorio. (RS-001)");
        if (provincia == null || provincia.isBlank())
            throw new ValidationException("La provincia de la subestación es obligatoria. (RS-001)");

        // RS-002: capacidad > 0
        if (capacidadMaximaMW <= 0)
            throw new ValidationException(
                    "La capacidad máxima debe ser mayor que cero. Valor recibido: " + capacidadMaximaMW + " (RS-002)");

        // Unicidad de nombre
        if (dao.existeConNombre(nombre.trim()))
            throw new ValidationException("Ya existe una subestación con el nombre '" + nombre + "'.");

        Subestacion s = new Subestacion(nombre.trim(), provincia.trim(),
                latitud, longitud, capacidadMaximaMW);
        return dao.guardar(s);
    }

    /**
     * Busca una subestación por id.
     *
     * @param id el id de la subestación.
     * @return Optional con la subestación si existe, vacío si no.
     */
    public Optional<Subestacion> buscarPorId(Long id) {
        return dao.buscarPorId(id);
    }

    /**
     * Devuelve todas las subestaciones registradas.
     *
     * @return lista con todas las subestaciones.
     */
    public List<Subestacion> listarTodas() {
        return dao.listarTodas();
    }

    /**
     * Devuelve las subestaciones conectadas a la dada.
     *
     * @param subestacionId id de la subestación.
     * @return lista con las subestaciones conectadas.
     * @throws ValidationException si la subestación no existe.
     */
    public List<Subestacion> listarConectadas(Long subestacionId) {
        if (!dao.buscarPorId(subestacionId).isPresent())
            throw new ValidationException("No existe ninguna subestación con ID " + subestacionId + ".");
        return dao.listarConectadas(subestacionId);
    }

    /**
     * Actualiza los datos de una subestación.
     *
     * @param id id de la subestación a actualizar.
     * @param nombre nombre nuevo.
     * @param provincia provincia nueva.
     * @param latitud latitud nueva.
     * @param longitud longitud nueva.
     * @param capacidadMaximaMW capacidad nueva (debe ser mayor que 0).
     * @return la subestación ya actualizada.
     * @throws ValidationException si los datos no son válidos o no existe.
     */
    public Subestacion actualizar(Long id, String nombre, String provincia,
                                  double latitud, double longitud, double capacidadMaximaMW) {
        Subestacion s = dao.buscarPorId(id)
                .orElseThrow(() -> new ValidationException("No existe ninguna subestación con ID " + id + "."));

        if (nombre == null || nombre.isBlank())
            throw new ValidationException("El nombre es obligatorio. (RS-001)");
        if (provincia == null || provincia.isBlank())
            throw new ValidationException("La provincia es obligatoria. (RS-001)");
        if (capacidadMaximaMW <= 0)
            throw new ValidationException("La capacidad máxima debe ser > 0. (RS-002)");

        // Comprueba unicidad sólo si cambió el nombre
        if (!s.getNombre().equalsIgnoreCase(nombre.trim()) && dao.existeConNombre(nombre.trim()))
            throw new ValidationException("Ya existe una subestación con el nombre '" + nombre + "'.");

        s.setNombre(nombre.trim());
        s.setProvincia(provincia.trim());
        s.setLatitud(latitud);
        s.setLongitud(longitud);
        s.setCapacidadMaximaMW(capacidadMaximaMW);
        return dao.actualizar(s);
    }

    /**
     * Borra una subestación por id.
     *
     * @param id id de la subestación.
     * @return true si se borra correctamente.
     * @throws ValidationException si no existe.
     */
    public boolean eliminar(Long id) {
        if (!dao.buscarPorId(id).isPresent())
            throw new ValidationException("No existe ninguna subestación con ID " + id + ".");
        return dao.eliminar(id);
    }
}
package service;

import dao.ContadorDAO;
import dao.ContratoSuministroDAO;
import model.Contador;
import model.ContratoSuministro;
import util.ValidationException;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Clase de servicio para los contadores.
 * Comprueba que los datos sean válidos (número de serie único,
 * contrato existente y sin contador previo) antes de guardar nada.
 */
public class ContadorService {

    /** DAO para acceder a los contadores. */
    private final ContadorDAO dao = new ContadorDAO();
    /** DAO para comprobar el contrato al que se asocia el contador. */
    private final ContratoSuministroDAO contratoDAO = new ContratoSuministroDAO();

    /**
     * Registra un contador nuevo.
     *
     * @param numeroSerie número de serie (obligatorio y único).
     * @param modelo modelo (obligatorio).
     * @param fechaInstalacion fecha de instalación (obligatoria).
     * @param contratoId id del contrato al que se asocia.
     * @return el contador ya registrado.
     * @throws ValidationException si algún dato no es válido o el contrato ya tenía contador.
     */
    public Contador registrar(String numeroSerie, String modelo,
                              LocalDate fechaInstalacion, Long contratoId) {
        if (numeroSerie == null || numeroSerie.isBlank())
            throw new ValidationException("El número de serie es obligatorio.");
        if (modelo == null || modelo.isBlank())
            throw new ValidationException("El modelo es obligatorio.");
        if (fechaInstalacion == null)
            throw new ValidationException("La fecha de instalación es obligatoria.");

        // RS-005
        if (dao.existeConNumeroSerie(numeroSerie.trim()))
            throw new ValidationException(
                    "Ya existe un contador con el número de serie '" + numeroSerie + "'. (RS-005)");

        ContratoSuministro contrato = contratoDAO.buscarPorId(contratoId)
                .orElseThrow(() -> new ValidationException("No existe ningún contrato con ID " + contratoId + "."));

        // Un contrato sólo puede tener un contador
        if (dao.buscarPorContrato(contratoId).isPresent())
            throw new ValidationException("El contrato " + contratoId + " ya tiene un contador asignado.");

        return dao.guardar(new Contador(numeroSerie.trim(), modelo.trim(), fechaInstalacion, contrato));
    }

    /**
     * Busca un contador por id.
     *
     * @param id id del contador.
     * @return Optional con el contador si existe, vacío si no.
     */
    public Optional<Contador> buscarPorId(Long id) {
        return dao.buscarPorId(id);
    }

    /**
     * Devuelve el contador asociado a un contrato (RF-007).
     *
     * @param contratoId id del contrato.
     * @return Optional con el contador del contrato (vacío si no tiene).
     * @throws ValidationException si el contrato no existe.
     */
    public Optional<Contador> buscarPorContrato(Long contratoId) {
        if (!contratoDAO.buscarPorId(contratoId).isPresent())
            throw new ValidationException("No existe ningún contrato con ID " + contratoId + ".");
        return dao.buscarPorContrato(contratoId);
    }

    /**
     * Actualiza los datos de un contador.
     *
     * @param id id del contador a actualizar.
     * @param numeroSerie nuevo número de serie (único).
     * @param modelo nuevo modelo.
     * @param fechaInstalacion nueva fecha de instalación.
     * @param contratoId nuevo id de contrato.
     * @return el contador ya actualizado.
     * @throws ValidationException si los datos no son válidos.
     */
    public Contador actualizar(Long id, String numeroSerie, String modelo,
                               LocalDate fechaInstalacion, Long contratoId) {
        Contador c = dao.buscarPorId(id)
                .orElseThrow(() -> new ValidationException("No existe ningún contador con ID " + id + "."));

        if (numeroSerie == null || numeroSerie.isBlank())
            throw new ValidationException("El número de serie es obligatorio.");

        // RS-005: unicidad sólo si cambió
        if (!c.getNumeroSerie().equalsIgnoreCase(numeroSerie.trim())
                && dao.existeConNumeroSerie(numeroSerie.trim()))
            throw new ValidationException(
                    "Ya existe un contador con el número de serie '" + numeroSerie + "'. (RS-005)");

        ContratoSuministro contrato = contratoDAO.buscarPorId(contratoId)
                .orElseThrow(() -> new ValidationException("No existe ningún contrato con ID " + contratoId + "."));

        c.setNumeroSerie(numeroSerie.trim());
        c.setModelo(modelo.trim());
        c.setFechaInstalacion(fechaInstalacion);
        c.setContrato(contrato);
        return dao.actualizar(c);
    }

    /**
     * Borra un contador por id.
     *
     * @param id id del contador a borrar.
     * @return true si se borra correctamente.
     * @throws ValidationException si no existe.
     */
    public boolean eliminar(Long id) {
        if (!dao.buscarPorId(id).isPresent())
            throw new ValidationException("No existe ningún contador con ID " + id + ".");
        return dao.eliminar(id);
    }
}

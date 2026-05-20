package service;

import dao.ContratoSuministroDAO;
import dao.TitularDAO;
import model.ContratoSuministro;
import model.Titular;
import util.ValidationException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Clase de servicio para los contratos de suministro.
 * Comprueba que los datos sean correctos (potencia mayor que 0,
 * código único, titular existente, etc.) antes de pasarlos a la DAO.
 */
public class ContratoSuministroService {

    /** DAO para acceder a los contratos. */
    private final ContratoSuministroDAO dao = new ContratoSuministroDAO();
    /** DAO para comprobar el titular del contrato. */
    private final TitularDAO titularDAO = new TitularDAO();

    /**
     * Registra un contrato nuevo.
     *
     * @param codigoContrato código (obligatorio y único).
     * @param tarifa tarifa (obligatoria).
     * @param fechaAlta fecha de alta (obligatoria).
     * @param potenciaKW potencia en kW (debe ser mayor que 0).
     * @param titularId id del titular dueño del contrato.
     * @return el contrato ya registrado.
     * @throws ValidationException si algún dato no es válido.
     */
    public ContratoSuministro registrar(String codigoContrato, String tarifa,
                                        LocalDate fechaAlta, double potenciaKW,
                                        Long titularId) {
        if (codigoContrato == null || codigoContrato.isBlank())
            throw new ValidationException("El código de contrato es obligatorio.");
        if (tarifa == null || tarifa.isBlank())
            throw new ValidationException("La tarifa es obligatoria.");
        if (fechaAlta == null)
            throw new ValidationException("La fecha de alta es obligatoria.");

        // RS-002
        if (potenciaKW <= 0)
            throw new ValidationException(
                    "La potencia contratada debe ser mayor que cero. Valor: " + potenciaKW + " (RS-002)");

        if (dao.existeConCodigo(codigoContrato.trim()))
            throw new ValidationException("Ya existe un contrato con el código '" + codigoContrato + "'.");

        Titular titular = titularDAO.buscarPorId(titularId)
                .orElseThrow(() -> new ValidationException("No existe ningún titular con ID " + titularId + "."));

        return dao.guardar(new ContratoSuministro(
                codigoContrato.trim(), tarifa.trim(), fechaAlta, potenciaKW, titular));
    }

    /**
     * Busca un contrato por id.
     *
     * @param id id del contrato.
     * @return Optional con el contrato si existe, vacío si no.
     */
    public Optional<ContratoSuministro> buscarPorId(Long id) {
        return dao.buscarPorId(id);
    }

    /**
     * Lista todos los contratos de un titular dado.
     *
     * @param titularId id del titular.
     * @return lista con los contratos del titular.
     * @throws ValidationException si el titular no existe.
     */
    public List<ContratoSuministro> listarPorTitular(Long titularId) {
        if (!titularDAO.buscarPorId(titularId).isPresent())
            throw new ValidationException("No existe ningún titular con ID " + titularId + ".");
        return dao.listarPorTitular(titularId);
    }

    /**
     * Actualiza los datos de un contrato.
     *
     * @param id id del contrato a actualizar.
     * @param codigoContrato código nuevo.
     * @param tarifa tarifa nueva.
     * @param fechaAlta fecha de alta nueva.
     * @param potenciaKW potencia (mayor que 0).
     * @param titularId id del nuevo titular.
     * @return el contrato actualizado.
     * @throws ValidationException si los datos no son válidos.
     */
    public ContratoSuministro actualizar(Long id, String codigoContrato, String tarifa,
                                         LocalDate fechaAlta, double potenciaKW, Long titularId) {
        ContratoSuministro c = dao.buscarPorId(id)
                .orElseThrow(() -> new ValidationException("No existe ningún contrato con ID " + id + "."));

        if (potenciaKW <= 0)
            throw new ValidationException("La potencia debe ser > 0. (RS-002)");
        if (!c.getCodigoContrato().equalsIgnoreCase(codigoContrato.trim())
                && dao.existeConCodigo(codigoContrato.trim()))
            throw new ValidationException("Ya existe un contrato con el código '" + codigoContrato + "'.");

        Titular titular = titularDAO.buscarPorId(titularId)
                .orElseThrow(() -> new ValidationException("No existe ningún titular con ID " + titularId + "."));

        c.setCodigoContrato(codigoContrato.trim());
        c.setTarifa(tarifa.trim());
        c.setFechaAlta(fechaAlta);
        c.setPotenciaContratadaKW(potenciaKW);
        c.setTitular(titular);
        return dao.actualizar(c);
    }

    /**
     * Borra un contrato por id.
     *
     * @param id id del contrato a borrar.
     * @return true si se borra correctamente.
     * @throws ValidationException si no existe.
     */
    public boolean eliminar(Long id) {
        if (!dao.buscarPorId(id).isPresent())
            throw new ValidationException("No existe ningún contrato con ID " + id + ".");
        return dao.eliminar(id);
    }
}

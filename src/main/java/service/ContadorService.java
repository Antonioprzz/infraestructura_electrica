package service;

import dao.ContadorDAO;
import dao.ContratoSuministroDAO;
import model.Contador;
import model.ContratoSuministro;
import util.ValidationException;

import java.time.LocalDate;
import java.util.Optional;

public class ContadorService {

    private final ContadorDAO dao = new ContadorDAO();
    private final ContratoSuministroDAO contratoDAO = new ContratoSuministroDAO();

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

    public Optional<Contador> buscarPorId(Long id) {
        return dao.buscarPorId(id);
    }

    /** RF-007 */
    public Optional<Contador> buscarPorContrato(Long contratoId) {
        if (!contratoDAO.buscarPorId(contratoId).isPresent())
            throw new ValidationException("No existe ningún contrato con ID " + contratoId + ".");
        return dao.buscarPorContrato(contratoId);
    }

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

    public boolean eliminar(Long id) {
        if (!dao.buscarPorId(id).isPresent())
            throw new ValidationException("No existe ningún contador con ID " + id + ".");
        return dao.eliminar(id);
    }
}

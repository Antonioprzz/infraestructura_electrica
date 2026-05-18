package service;

import dao.ContratoSuministroDAO;
import dao.TitularDAO;
import model.ContratoSuministro;
import model.Titular;
import util.ValidationException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class ContratoSuministroService {

    private final ContratoSuministroDAO dao = new ContratoSuministroDAO();
    private final TitularDAO titularDAO = new TitularDAO();

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

    public Optional<ContratoSuministro> buscarPorId(Long id) {
        return dao.buscarPorId(id);
    }

    public List<ContratoSuministro> listarPorTitular(Long titularId) {
        if (!titularDAO.buscarPorId(titularId).isPresent())
            throw new ValidationException("No existe ningún titular con ID " + titularId + ".");
        return dao.listarPorTitular(titularId);
    }

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

    public boolean eliminar(Long id) {
        if (!dao.buscarPorId(id).isPresent())
            throw new ValidationException("No existe ningún contrato con ID " + id + ".");
        return dao.eliminar(id);
    }
}

package service;

import dao.LineaTransporteDAO;
import dao.SubestacionDAO;
import model.LineaTransporte;
import model.Subestacion;
import util.ValidationException;

import java.util.List;
import java.util.Optional;

public class LineaTransporteService {

    private final LineaTransporteDAO dao = new LineaTransporteDAO();
    private final SubestacionDAO subDAO = new SubestacionDAO();

    public LineaTransporte registrar(String codigo, double longitudKm, double voltajeKV,
                                     int anioInstalacion, String tramo,
                                     Long idOrigen, Long idDestino) {
        // RS-006
        if (longitudKm <= 0)
            throw new ValidationException(
                    "La longitud debe ser mayor que cero. Valor: " + longitudKm + " (RS-006)");
        if (codigo == null || codigo.isBlank())
            throw new ValidationException("El código de la línea es obligatorio.");
        if (tramo == null || tramo.isBlank())
            throw new ValidationException("El tramo es obligatorio.");
        if (dao.existeConCodigo(codigo.trim()))
            throw new ValidationException("Ya existe una línea con el código '" + codigo + "'.");

        Subestacion origen = subDAO.buscarPorId(idOrigen)
                .orElseThrow(() -> new ValidationException("Subestación origen no encontrada (ID=" + idOrigen + ")."));
        Subestacion destino = subDAO.buscarPorId(idDestino)
                .orElseThrow(() -> new ValidationException("Subestación destino no encontrada (ID=" + idDestino + ")."));
        if (idOrigen.equals(idDestino))
            throw new ValidationException("La subestación origen y destino no pueden ser la misma.");

        LineaTransporte linea = new LineaTransporte(codigo.trim(), longitudKm, voltajeKV,
                anioInstalacion, tramo.trim(), origen, destino);
        return dao.guardar(linea);
    }

    public Optional<LineaTransporte> buscarPorId(Long id) {
        return dao.buscarPorId(id);
    }

    public List<LineaTransporte> listarTodas() {
        return dao.listarTodas();
    }

    public LineaTransporte actualizar(Long id, String codigo, double longitudKm, double voltajeKV,
                                      int anioInstalacion, String tramo,
                                      Long idOrigen, Long idDestino) {
        LineaTransporte l = dao.buscarPorId(id)
                .orElseThrow(() -> new ValidationException("No existe ninguna línea con ID " + id + "."));

        if (longitudKm <= 0)
            throw new ValidationException("La longitud debe ser > 0. (RS-006)");
        if (codigo == null || codigo.isBlank())
            throw new ValidationException("El código es obligatorio.");
        if (!l.getCodigo().equalsIgnoreCase(codigo.trim()) && dao.existeConCodigo(codigo.trim()))
            throw new ValidationException("Ya existe una línea con el código '" + codigo + "'.");

        Subestacion origen = subDAO.buscarPorId(idOrigen)
                .orElseThrow(() -> new ValidationException("Subestación origen no encontrada (ID=" + idOrigen + ")."));
        Subestacion destino = subDAO.buscarPorId(idDestino)
                .orElseThrow(() -> new ValidationException("Subestación destino no encontrada (ID=" + idDestino + ")."));

        l.setCodigo(codigo.trim());
        l.setLongitudKm(longitudKm);
        l.setVoltajeKV(voltajeKV);
        l.setAnioInstalacion(anioInstalacion);
        l.setTramo(tramo.trim());
        l.setSubestacionOrigen(origen);
        l.setSubestacionDestino(destino);
        return dao.actualizar(l);
    }

    public boolean eliminar(Long id) {
        if (!dao.buscarPorId(id).isPresent())
            throw new ValidationException("No existe ninguna línea con ID " + id + ".");
        return dao.eliminar(id);
    }
}

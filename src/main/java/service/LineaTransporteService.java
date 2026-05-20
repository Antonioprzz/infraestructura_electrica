package service;

import dao.LineaTransporteDAO;
import dao.SubestacionDAO;
import model.LineaTransporte;
import model.Subestacion;
import util.ValidationException;

import java.util.List;
import java.util.Optional;

/**
 * Clase de servicio para las líneas de transporte.
 * Valida los datos antes de pasarlos a la DAO y comprueba que las
 * subestaciones de origen y destino existan y no sean la misma.
 */
public class LineaTransporteService {

    /** DAO para acceder a las líneas en la BBDD. */
    private final LineaTransporteDAO dao = new LineaTransporteDAO();
    /** DAO para comprobar las subestaciones origen/destino. */
    private final SubestacionDAO subDAO = new SubestacionDAO();

    /**
     * Registra una línea nueva, validando los datos.
     *
     * @param codigo código identificativo (obligatorio y único).
     * @param longitudKm longitud en km (debe ser mayor que 0).
     * @param voltajeKV voltaje en kV.
     * @param anioInstalacion año de instalación.
     * @param tramo tramo o ruta (obligatorio).
     * @param idOrigen id de la subestación de origen.
     * @param idDestino id de la subestación de destino.
     * @return la línea ya registrada.
     * @throws ValidationException si algún dato no es válido.
     */
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

    /**
     * Busca una línea por id.
     *
     * @param id id de la línea.
     * @return Optional con la línea si existe, vacío si no.
     */
    public Optional<LineaTransporte> buscarPorId(Long id) {
        return dao.buscarPorId(id);
    }

    /**
     * Devuelve todas las líneas registradas.
     *
     * @return lista con todas las líneas.
     */
    public List<LineaTransporte> listarTodas() {
        return dao.listarTodas();
    }

    /**
     * Actualiza los datos de una línea.
     *
     * @param id id de la línea a actualizar.
     * @param codigo código (no se puede repetir si cambia).
     * @param longitudKm longitud en km (debe ser mayor que 0).
     * @param voltajeKV voltaje en kV.
     * @param anioInstalacion año de instalación.
     * @param tramo tramo o ruta.
     * @param idOrigen id de la subestación de origen.
     * @param idDestino id de la subestación de destino.
     * @return la línea actualizada.
     * @throws ValidationException si los datos no son válidos.
     */
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

    /**
     * Borra una línea por id.
     *
     * @param id id de la línea a borrar.
     * @return true si se borra correctamente.
     * @throws ValidationException si no existe.
     */
    public boolean eliminar(Long id) {
        if (!dao.buscarPorId(id).isPresent())
            throw new ValidationException("No existe ninguna línea con ID " + id + ".");
        return dao.eliminar(id);
    }
}

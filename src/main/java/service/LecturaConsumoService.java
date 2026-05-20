package service;

import dao.ContadorDAO;
import dao.LecturaConsumoDAO;
import model.Contador;
import model.LecturaConsumo;
import model.LecturaConsumo.OrigenLectura;
import util.ValidationException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


/**
 * Clase de servicio para las lecturas de consumo.
 * Comprueba que los datos de la lectura sean válidos
 * (valor no negativo, contador existente...) antes de guardarlos.
 */
public class LecturaConsumoService {

    /** DAO para acceder a las lecturas. */
    private final LecturaConsumoDAO dao = new LecturaConsumoDAO();
    /** DAO para comprobar el contador al que se asocia la lectura. */
    private final ContadorDAO contadorDAO = new ContadorDAO();

    /**
     * Registra una lectura nueva.
     *
     * @param fechaHora fecha y hora de la lectura (obligatoria).
     * @param valorKWh valor en kWh (no negativo).
     * @param origen AUTOMATICO o MANUAL (obligatorio).
     * @param contadorId id del contador al que pertenece.
     * @return la lectura ya registrada.
     * @throws ValidationException si algún dato no es válido.
     */
    public LecturaConsumo registrar(LocalDateTime fechaHora, double valorKWh,
                                    OrigenLectura origen, Long contadorId) {
        if (fechaHora == null)
            throw new ValidationException("La fecha y hora de la lectura son obligatorias.");
        if (origen == null)
            throw new ValidationException("El origen de la lectura es obligatorio (AUTOMATICO/MANUAL).");

        // RS-003
        if (valorKWh < 0)
            throw new ValidationException(
                    "El valor de la lectura no puede ser negativo. Valor: " + valorKWh + " (RS-003)");

        Contador contador = contadorDAO.buscarPorId(contadorId)
                .orElseThrow(() -> new ValidationException("No existe ningún contador con ID " + contadorId + "."));

        return dao.guardar(new LecturaConsumo(fechaHora, valorKWh, origen, contador));
    }

    /**
     * Busca una lectura por id.
     *
     * @param id id de la lectura.
     * @return Optional con la lectura si existe, vacío si no.
     */
    public Optional<LecturaConsumo> buscarPorId(Long id) {
        return dao.buscarPorId(id);
    }

    /**
     * Lista todas las lecturas de un contador.
     *
     * @param contadorId id del contador.
     * @return lista con sus lecturas.
     * @throws ValidationException si el contador no existe.
     */
    public List<LecturaConsumo> listarPorContador(Long contadorId) {
        if (!contadorDAO.buscarPorId(contadorId).isPresent())
            throw new ValidationException("No existe ningún contador con ID " + contadorId + ".");
        return dao.listarPorContador(contadorId);
    }

    /**
     * Actualiza una lectura existente.
     *
     * @param id id de la lectura a actualizar.
     * @param fechaHora nueva fecha y hora.
     * @param valorKWh nuevo valor (no negativo).
     * @param origen nuevo origen (AUTOMATICO/MANUAL).
     * @param contadorId nuevo id de contador.
     * @return la lectura actualizada.
     * @throws ValidationException si los datos no son válidos.
     */
    public LecturaConsumo actualizar(Long id, LocalDateTime fechaHora,
                                     double valorKWh, OrigenLectura origen, Long contadorId) {
        LecturaConsumo l = dao.buscarPorId(id)
                .orElseThrow(() -> new ValidationException("No existe ninguna lectura con ID " + id + "."));

        if (valorKWh < 0)
            throw new ValidationException("El valor no puede ser negativo. (RS-003)");

        Contador contador = contadorDAO.buscarPorId(contadorId)
                .orElseThrow(() -> new ValidationException("No existe ningún contador con ID " + contadorId + "."));

        l.setFechaHora(fechaHora);
        l.setValorKWh(valorKWh);
        l.setOrigen(origen);
        l.setContador(contador);
        return dao.actualizar(l);
    }

    /**
     * Borra una lectura por id.
     *
     * @param id id de la lectura a borrar.
     * @return true si se borra correctamente.
     * @throws ValidationException si no existe.
     */
    public boolean eliminar(Long id) {
        if (!dao.buscarPorId(id).isPresent())
            throw new ValidationException("No existe ninguna lectura con ID " + id + ".");
        return dao.eliminar(id);
    }
}

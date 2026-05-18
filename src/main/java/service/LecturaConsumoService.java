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


public class LecturaConsumoService {

    private final LecturaConsumoDAO dao = new LecturaConsumoDAO();
    private final ContadorDAO contadorDAO = new ContadorDAO();

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

    public Optional<LecturaConsumo> buscarPorId(Long id) {
        return dao.buscarPorId(id);
    }

    public List<LecturaConsumo> listarPorContador(Long contadorId) {
        if (!contadorDAO.buscarPorId(contadorId).isPresent())
            throw new ValidationException("No existe ningún contador con ID " + contadorId + ".");
        return dao.listarPorContador(contadorId);
    }

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

    public boolean eliminar(Long id) {
        if (!dao.buscarPorId(id).isPresent())
            throw new ValidationException("No existe ninguna lectura con ID " + id + ".");
        return dao.eliminar(id);
    }
}

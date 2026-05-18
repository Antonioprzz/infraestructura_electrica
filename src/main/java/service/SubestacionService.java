package service;

import dao.SubestacionDAO;
import model.Subestacion;
import util.ValidationException;

import java.util.List;
import java.util.Optional;

public class SubestacionService {

    private final SubestacionDAO dao = new SubestacionDAO();

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

    public Optional<Subestacion> buscarPorId(Long id) {
        return dao.buscarPorId(id);
    }

    public List<Subestacion> listarTodas() {
        return dao.listarTodas();
    }

    public List<Subestacion> listarConectadas(Long subestacionId) {
        if (!dao.buscarPorId(subestacionId).isPresent())
            throw new ValidationException("No existe ninguna subestación con ID " + subestacionId + ".");
        return dao.listarConectadas(subestacionId);
    }

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

    public boolean eliminar(Long id) {
        if (!dao.buscarPorId(id).isPresent())
            throw new ValidationException("No existe ninguna subestación con ID " + id + ".");
        return dao.eliminar(id);
    }
}
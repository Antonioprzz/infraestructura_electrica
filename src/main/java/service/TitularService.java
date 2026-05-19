package service;

import dao.TitularDAO;
import model.Titular;
import util.ValidationException;

import java.util.List;
import java.util.Optional;

public class TitularService {

    private final TitularDAO dao = new TitularDAO();

    // ── Validación de DNI/NIF español ────────────────────────────────────────
    // Formato: 8 dígitos + 1 letra (cualquiera)
    private static void validarNif(String nif) {
        if (nif == null || nif.isBlank())
            throw new ValidationException("El NIF es obligatorio.");

        String n = nif.trim().toUpperCase();

        // Formato: exactamente 8 dígitos seguidos de cualquier letra
        if (!n.matches("\\d{8}[A-Z]"))
            throw new ValidationException(
                    "El NIF '" + nif.trim() + "' no tiene un formato válido. " +
                            "Debe contener 8 dígitos seguidos de una letra (ej: 12345678Z).");
    }
    // ─────────────────────────────────────────────────────────────────────────

    public Titular registrar(String nombreCompleto, String nif, String direccion, String email) {
        if (nombreCompleto == null || nombreCompleto.isBlank())
            throw new ValidationException("El nombre completo o razón social es obligatorio.");

        validarNif(nif);

        // RS-004
        if (dao.existeConNif(nif.trim()))
            throw new ValidationException(
                    "Ya existe un titular con el NIF '" + nif + "'. (RS-004)");

        return dao.guardar(new Titular(nombreCompleto.trim(), nif.trim().toUpperCase(),
                direccion, email));
    }

    public Optional<Titular> buscarPorId(Long id) {
        return dao.buscarPorId(id);
    }

    public List<Titular> listarTodos() {
        return dao.listarTodos();
    }

    public Titular actualizar(Long id, String nombreCompleto, String nif,
                              String direccion, String email) {
        Titular t = dao.buscarPorId(id)
                .orElseThrow(() -> new ValidationException("No existe ningún titular con ID " + id + "."));

        if (nombreCompleto == null || nombreCompleto.isBlank())
            throw new ValidationException("El nombre es obligatorio.");

        validarNif(nif);

        // RS-004: unicidad, sólo si cambió
        if (!t.getNif().equalsIgnoreCase(nif.trim()) && dao.existeConNif(nif.trim()))
            throw new ValidationException("Ya existe un titular con el NIF '" + nif + "'. (RS-004)");

        t.setNombreCompleto(nombreCompleto.trim());
        t.setNif(nif.trim().toUpperCase());
        t.setDireccion(direccion);
        t.setEmail(email);
        return dao.actualizar(t);
    }

    public boolean eliminar(Long id) {
        if (!dao.buscarPorId(id).isPresent())
            throw new ValidationException("No existe ningún titular con ID " + id + ".");
        return dao.eliminar(id);
    }
}
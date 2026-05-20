package util;

/**
 * Excepción propia que se lanza cuando algún dato no cumple las reglas
 * de validación del programa (por ejemplo, un campo obligatorio vacío,
 * un valor negativo donde no debería, un duplicado, etc.).
 *
 * Hereda de RuntimeException, así que no hay que declararla
 * obligatoriamente con "throws" en cada método.
 */
public class ValidationException extends RuntimeException {

    /**
     * Crea la excepción con un mensaje que explica el error.
     *
     * @param message texto con el motivo del error.
     */
    public ValidationException(String message) {
        super(message);
    }

    /**
     * Crea la excepción con un mensaje y otra excepción que la causó.
     *
     * @param message texto con el motivo del error.
     * @param cause excepción original que provocó este error.
     */
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}

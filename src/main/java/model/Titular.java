package model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa al titular (cliente o empresa) de los contratos.
 * Es una entidad JPA, se guarda en la tabla "titulares".
 * Cada titular tiene un nombre, un NIF (que no se puede repetir),
 * y opcionalmente dirección y email. Un titular puede tener varios contratos.
 */
@Entity
@Table(name = "titulares")
public class Titular {

    /** Identificador único del titular. Se genera automáticamente. */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /** Nombre completo o razón social. Es obligatorio. */
    @Column(nullable = false)
    private String nombreCompleto;

    /** NIF/DNI del titular. No se puede repetir. */
    @Column(nullable = false, unique = true)
    private String nif;

    /** Dirección del titular (opcional). */
    private String direccion;

    /** Email del titular (opcional). */
    private String email;

    /** Lista de contratos que tiene el titular. */
    @OneToMany(mappedBy = "titular", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ContratoSuministro> contratos = new ArrayList<>();

    // Constructores

    /** Constructor vacío (necesario para JPA). */
    public Titular() {}

    /**
     * Constructor con todos los datos del titular.
     *
     * @param nombreCompleto nombre o razón social.
     * @param nif NIF/DNI.
     * @param direccion dirección (puede ser null).
     * @param email email (puede ser null).
     */
    public Titular(String nombreCompleto, String nif, String direccion, String email) {
        this.nombreCompleto = nombreCompleto;
        this.nif = nif;
        this.direccion = direccion;
        this.email = email;
    }

    // Getters y Setters

    /** @return el id del titular. */
    public Long getId() { return id; }

    /** @return el nombre completo o razón social. */
    public String getNombreCompleto() { return nombreCompleto; }
    /** Cambia el nombre o razón social. */
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    /** @return el NIF del titular. */
    public String getNif() { return nif; }
    /** Cambia el NIF. */
    public void setNif(String nif) { this.nif = nif; }

    /** @return la dirección. */
    public String getDireccion() { return direccion; }
    /** Cambia la dirección. */
    public void setDireccion(String direccion) { this.direccion = direccion; }

    /** @return el email. */
    public String getEmail() { return email; }
    /** Cambia el email. */
    public void setEmail(String email) { this.email = email; }

    /** @return la lista de contratos del titular. */
    public List<ContratoSuministro> getContratos() { return contratos; }

    /** Devuelve un texto con los datos del titular, útil para imprimir. */
    @Override
    public String toString() {
        return String.format(
                "Titular{id=%d, nombre='%s', NIF='%s', direccion='%s', email='%s'}",
                id, nombreCompleto, nif, direccion, email);
    }
}

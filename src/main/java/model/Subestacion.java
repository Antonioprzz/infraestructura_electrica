package model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa una subestación eléctrica.
 * Es una entidad JPA, por lo que se guarda en la tabla "subestaciones".
 * Una subestación tiene un nombre, está en una provincia, tiene unas
 * coordenadas (latitud/longitud) y una capacidad máxima en MW.
 * Además, puede tener líneas de transporte que salen (origen) y que llegan (destino).
 */
@Entity
@Table(name = "subestaciones")
public class Subestacion {

    /** Identificador único de la subestación. Lo genera la BBDD automáticamente. */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /** Nombre de la subestación. No puede repetirse y es obligatorio. */
    @Column(nullable = false, unique = true)
    private String nombre;

    /** Provincia donde está la subestación. Obligatoria. */
    @Column(nullable = false)
    private String provincia;

    /** Coordenada de latitud (en grados). */
    private double latitud;

    /** Coordenada de longitud (en grados). */
    private double longitud;

    /** Capacidad máxima de la subestación en megavatios (MW). */
    @Column(nullable = false)
    private double capacidadMaximaMW;

    /** Lista de líneas de transporte que SALEN de esta subestación (es origen). */
    @OneToMany(mappedBy = "subestacionOrigen", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LineaTransporte> lineasOrigen = new ArrayList<>();

    /** Lista de líneas de transporte que LLEGAN a esta subestación (es destino). */
    @OneToMany(mappedBy = "subestacionDestino", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LineaTransporte> lineasDestino = new ArrayList<>();

    // Constructores

    /** Constructor vacío que necesita JPA para crear los objetos. */
    public Subestacion() {}

    /**
     * Constructor con todos los datos para crear una subestación nueva.
     *
     * @param nombre nombre de la subestación.
     * @param provincia provincia donde está.
     * @param latitud coordenada de latitud.
     * @param longitud coordenada de longitud.
     * @param capacidadMaximaMW capacidad máxima en MW.
     */
    public Subestacion(String nombre, String provincia, double latitud,
                       double longitud, double capacidadMaximaMW) {
        this.nombre = nombre;
        this.provincia = provincia;
        this.latitud = latitud;
        this.longitud = longitud;
        this.capacidadMaximaMW = capacidadMaximaMW;
    }

    // Getters y Setters

    /** @return el id de la subestación. */
    public Long getId() { return id; }

    /** @return el nombre de la subestación. */
    public String getNombre() {
        return nombre;
    }
    /** Cambia el nombre de la subestación. */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /** @return la provincia donde está la subestación. */
    public String getProvincia() {
        return provincia;
    }
    /** Cambia la provincia. */
    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    /** @return la latitud (coordenada). */
    public double getLatitud() {
        return latitud;
    }
    /** Cambia la latitud. */
    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    /** @return la longitud (coordenada). */
    public double getLongitud() {
        return longitud;
    }
    /** Cambia la longitud. */
    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    /** @return la capacidad máxima en MW. */
    public double getCapacidadMaximaMW() {
        return capacidadMaximaMW;
    }
    /** Cambia la capacidad máxima. */
    public void setCapacidadMaximaMW(double capacidadMaximaMW) {
        this.capacidadMaximaMW = capacidadMaximaMW;
    }

    /** @return las líneas que salen de esta subestación. */
    public List<LineaTransporte> getLineasOrigen() {
        return lineasOrigen;
    }
    /** @return las líneas que llegan a esta subestación. */
    public List<LineaTransporte> getLineasDestino() {
        return lineasDestino;
    }

    /** Devuelve un texto con la información de la subestación, útil para imprimir. */
    @Override
    public String toString() {
        return String.format(
                "Subestacion{id=%d, nombre='%s', provincia='%s', " +
                        "coordenadas=(%.4f, %.4f), capacidadMax=%.2f MW}",
                id, nombre, provincia, latitud, longitud, capacidadMaximaMW);
    }
}

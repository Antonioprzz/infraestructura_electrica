package model;

import javax.persistence.*;

/**
 * Clase que representa una línea de transporte eléctrica.
 * Conecta dos subestaciones (una de origen y otra de destino) y guarda
 * datos como el código, longitud en km, voltaje en kV, año de instalación
 * y el tramo. Es una entidad JPA, se guarda en la tabla "lineas_transporte".
 */
@Entity
@Table(name = "lineas_transporte")
public class LineaTransporte {

    /** Identificador único de la línea. Se genera automáticamente. */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /** Código de la línea. No se puede repetir. */
    @Column(nullable = false, unique = true)
    private String codigo;

    /** Longitud de la línea en kilómetros. */
    @Column(nullable = false)
    private double longitudKm;

    /** Voltaje de la línea en kilovoltios (kV). */
    @Column(nullable = false)
    private double voltajeKV;

    /** Año en el que se instaló la línea. */
    @Column(nullable = false)
    private int anioInstalacion;

    /** Tramo (zona o ruta) de la línea. */
    @Column(nullable = false)
    private String tramo;

    /** Subestación de la que sale la línea. */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subestacion_origen_id", nullable = false)
    private Subestacion subestacionOrigen;

    /** Subestación a la que llega la línea. */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subestacion_destino_id", nullable = false)
    private Subestacion subestacionDestino;

    // Constructores

    /** Constructor vacío (necesario para JPA). */
    public LineaTransporte() {}

    /**
     * Constructor con todos los datos de la línea.
     *
     * @param codigo código identificativo.
     * @param longitudKm longitud en km.
     * @param voltajeKV voltaje en kV.
     * @param anioInstalacion año de instalación.
     * @param tramo tramo o ruta.
     * @param subestacionOrigen subestación de origen.
     * @param subestacionDestino subestación de destino.
     */
    public LineaTransporte(String codigo, double longitudKm, double voltajeKV,
                           int anioInstalacion, String tramo,
                           Subestacion subestacionOrigen, Subestacion subestacionDestino) {
        this.codigo = codigo;
        this.longitudKm = longitudKm;
        this.voltajeKV = voltajeKV;
        this.anioInstalacion = anioInstalacion;
        this.tramo = tramo;
        this.subestacionOrigen = subestacionOrigen;
        this.subestacionDestino = subestacionDestino;
    }

    // Getters y Setters

    /** @return el id de la línea. */
    public Long getId() { return id; }

    /** @return el código de la línea. */
    public String getCodigo() { return codigo; }
    /** Cambia el código de la línea. */
    public void setCodigo(String codigo) { this.codigo = codigo; }

    /** @return la longitud en km. */
    public double getLongitudKm() { return longitudKm; }
    /** Cambia la longitud (en km). */
    public void setLongitudKm(double longitudKm) { this.longitudKm = longitudKm; }

    /** @return el voltaje en kV. */
    public double getVoltajeKV() { return voltajeKV; }
    /** Cambia el voltaje (en kV). */
    public void setVoltajeKV(double voltajeKV) { this.voltajeKV = voltajeKV; }

    /** @return el año de instalación. */
    public int getAnioInstalacion() { return anioInstalacion; }
    /** Cambia el año de instalación. */
    public void setAnioInstalacion(int anioInstalacion) { this.anioInstalacion = anioInstalacion; }

    /** @return el tramo o ruta de la línea. */
    public String getTramo() { return tramo; }
    /** Cambia el tramo. */
    public void setTramo(String tramo) { this.tramo = tramo; }

    /** @return la subestación de origen. */
    public Subestacion getSubestacionOrigen() { return subestacionOrigen; }
    /** Cambia la subestación de origen. */
    public void setSubestacionOrigen(Subestacion subestacionOrigen) { this.subestacionOrigen = subestacionOrigen; }

    /** @return la subestación de destino. */
    public Subestacion getSubestacionDestino() { return subestacionDestino; }
    /** Cambia la subestación de destino. */
    public void setSubestacionDestino(Subestacion subestacionDestino) { this.subestacionDestino = subestacionDestino; }

    /** Devuelve un texto con los datos de la línea, útil para mostrarlos por pantalla. */
    @Override
    public String toString() {
        return String.format(
                "LineaTransporte{id=%d, codigo='%s', longitud=%.2f km, voltaje=%.2f kV, " +
                        "anioInstalacion=%d, tramo='%s', origen='%s', destino='%s'}",
                id, codigo, longitudKm, voltajeKV, anioInstalacion, tramo,
                subestacionOrigen != null ? subestacionOrigen.getNombre() : "N/A",
                subestacionDestino != null ? subestacionDestino.getNombre() : "N/A");
    }
}

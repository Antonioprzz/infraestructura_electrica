package model;

import model.Contador;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Clase que representa una lectura de consumo eléctrico.
 * Es una entidad JPA, se guarda en la tabla "lecturas_consumo".
 * Una lectura tiene una fecha y hora, un valor en kWh, un origen
 * (AUTOMÁTICO o MANUAL) y está asociada a un Contador.
 */
@Entity
@Table(name = "lecturas_consumo")
public class LecturaConsumo {

    /** Identificador único de la lectura. Se genera automáticamente. */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /** Fecha y hora en que se hizo la lectura. */
    @Column(nullable = false)
    private LocalDateTime fechaHora;

    /** Valor leído en kilovatios hora (kWh). */
    @Column(nullable = false)
    private double valorKWh;

    /** Origen de la lectura (automática o manual). */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrigenLectura origen;

    /** Contador al que pertenece esta lectura. */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "contador_id", nullable = false)
    private Contador contador;

    // Enum

    /**
     * Indica si la lectura fue tomada de manera AUTOMATICA
     * (por un sistema) o MANUAL (por una persona).
     */
    public enum OrigenLectura {
        AUTOMATICO,
        MANUAL
    }

    // Constructores

    /** Constructor vacío (necesario para JPA). */
    public LecturaConsumo() {}

    /**
     * Constructor con todos los datos de la lectura.
     *
     * @param fechaHora fecha y hora de la lectura.
     * @param valorKWh valor leído en kWh.
     * @param origen si la lectura es automática o manual.
     * @param contador contador al que pertenece.
     */
    public LecturaConsumo(LocalDateTime fechaHora, double valorKWh,
                          OrigenLectura origen, Contador contador) {
        this.fechaHora = fechaHora;
        this.valorKWh = valorKWh;
        this.origen = origen;
        this.contador = contador;
    }

    // Getters y Setters

    /** @return el id de la lectura. */
    public Long getId() { return id; }

    /** @return la fecha y hora de la lectura. */
    public LocalDateTime getFechaHora() { return fechaHora; }
    /** Cambia la fecha y hora de la lectura. */
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    /** @return el valor leído en kWh. */
    public double getValorKWh() { return valorKWh; }
    /** Cambia el valor en kWh. */
    public void setValorKWh(double valorKWh) { this.valorKWh = valorKWh; }

    /** @return el origen (AUTOMATICO o MANUAL). */
    public OrigenLectura getOrigen() { return origen; }
    /** Cambia el origen de la lectura. */
    public void setOrigen(OrigenLectura origen) { this.origen = origen; }

    /** @return el contador al que pertenece la lectura. */
    public Contador getContador() { return contador; }
    /** Cambia el contador asociado. */
    public void setContador(Contador contador) { this.contador = contador; }

    /** Devuelve un texto con los datos de la lectura, útil para mostrar por pantalla. */
    @Override
    public String toString() {
        return String.format(
                "LecturaConsumo{id=%d, fechaHora=%s, valor=%.3f kWh, origen=%s, contadorSerie='%s'}",
                id, fechaHora, valorKWh, origen,
                contador != null ? contador.getNumeroSerie() : "N/A");
    }
}

package model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa un contador eléctrico.
 * Es una entidad JPA, se guarda en la tabla "contadores".
 * Cada contador pertenece a un único contrato (relación 1 a 1) y puede
 * tener muchas lecturas de consumo asociadas.
 */
@Entity
@Table(name = "contadores")
public class Contador {

    /** Identificador único del contador. Se genera automáticamente. */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /** Número de serie del contador. No se puede repetir. */
    @Column(nullable = false, unique = true)
    private String numeroSerie;

    /** Modelo del contador. */
    @Column(nullable = false)
    private String modelo;

    /** Fecha en la que se instaló el contador. */
    @Column(nullable = false)
    private LocalDate fechaInstalacion;

    /** Contrato al que pertenece este contador (uno a uno). */
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "contrato_id", nullable = false, unique = true)
    private ContratoSuministro contrato;

    /** Lista de lecturas de consumo realizadas con este contador. */
    @OneToMany(mappedBy = "contador", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LecturaConsumo> lecturas = new ArrayList<>();

    // Constructores

    /** Constructor vacío (necesario para JPA). */
    public Contador() {}

    /**
     * Constructor con todos los datos del contador.
     *
     * @param numeroSerie número de serie único.
     * @param modelo modelo del contador.
     * @param fechaInstalacion fecha de instalación.
     * @param contrato contrato al que se asocia.
     */
    public Contador(String numeroSerie, String modelo, LocalDate fechaInstalacion,
                    ContratoSuministro contrato) {
        this.numeroSerie = numeroSerie;
        this.modelo = modelo;
        this.fechaInstalacion = fechaInstalacion;
        this.contrato = contrato;
    }

    // Getters y Setters

    /** @return el id del contador. */
    public Long getId() { return id; }

    /** @return el número de serie. */
    public String getNumeroSerie() { return numeroSerie; }
    /** Cambia el número de serie. */
    public void setNumeroSerie(String numeroSerie) { this.numeroSerie = numeroSerie; }

    /** @return el modelo del contador. */
    public String getModelo() { return modelo; }
    /** Cambia el modelo. */
    public void setModelo(String modelo) { this.modelo = modelo; }

    /** @return la fecha de instalación. */
    public LocalDate getFechaInstalacion() { return fechaInstalacion; }
    /** Cambia la fecha de instalación. */
    public void setFechaInstalacion(LocalDate fechaInstalacion) { this.fechaInstalacion = fechaInstalacion; }

    /** @return el contrato al que pertenece. */
    public ContratoSuministro getContrato() { return contrato; }
    /** Cambia el contrato al que pertenece. */
    public void setContrato(ContratoSuministro contrato) { this.contrato = contrato; }

    /** @return la lista de lecturas hechas con este contador. */
    public List<LecturaConsumo> getLecturas() { return lecturas; }

    /** Devuelve un texto con los datos del contador, útil para mostrarlo por pantalla. */
    @Override
    public String toString() {
        return String.format(
                "Contador{id=%d, numeroSerie='%s', modelo='%s', fechaInstalacion=%s, contratoId=%d}",
                id, numeroSerie, modelo, fechaInstalacion,
                contrato != null ? contrato.getId() : -1);
    }
}


package model;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Clase que representa un contrato de suministro eléctrico.
 * Es una entidad JPA, se guarda en la tabla "contratos_suministro".
 * Cada contrato pertenece a un Titular y puede tener un único Contador asociado.
 * Guarda el código del contrato, la tarifa, la fecha de alta y la potencia
 * contratada en kW.
 */
@Entity
@Table(name = "contratos_suministro")
public class ContratoSuministro {

    /** Identificador único del contrato. Se genera automáticamente. */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /** Código identificativo del contrato. No se puede repetir. */
    @Column(nullable = false, unique = true)
    private String codigoContrato;

    /** Tarifa contratada (por ejemplo "2.0TD"). */
    @Column(nullable = false)
    private String tarifa;

    /** Fecha en la que se dio de alta el contrato. */
    @Column(nullable = false)
    private LocalDate fechaAlta;

    /** Potencia contratada en kilovatios (kW). */
    @Column(nullable = false)
    private double potenciaContratadaKW;

    /** Titular dueño del contrato. */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "titular_id", nullable = false)
    private Titular titular;

    /** Contador asociado al contrato (puede no haber, pero como mucho uno). */
    @OneToOne(mappedBy = "contrato", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Contador contador;

    // Constructores

    /** Constructor vacío (necesario para JPA). */
    public ContratoSuministro() {}

    /**
     * Constructor con todos los datos del contrato.
     *
     * @param codigoContrato código identificativo.
     * @param tarifa tarifa contratada.
     * @param fechaAlta fecha de alta.
     * @param potenciaContratadaKW potencia en kW.
     * @param titular titular del contrato.
     */
    public ContratoSuministro(String codigoContrato, String tarifa,
                              LocalDate fechaAlta, double potenciaContratadaKW,
                              Titular titular) {
        this.codigoContrato = codigoContrato;
        this.tarifa = tarifa;
        this.fechaAlta = fechaAlta;
        this.potenciaContratadaKW = potenciaContratadaKW;
        this.titular = titular;
    }

    // Getters y Setters

    /** @return el id del contrato. */
    public Long getId() { return id; }

    /** @return el código del contrato. */
    public String getCodigoContrato() { return codigoContrato; }
    /** Cambia el código del contrato. */
    public void setCodigoContrato(String codigoContrato) { this.codigoContrato = codigoContrato; }

    /** @return la tarifa contratada. */
    public String getTarifa() { return tarifa; }
    /** Cambia la tarifa. */
    public void setTarifa(String tarifa) { this.tarifa = tarifa; }

    /** @return la fecha de alta. */
    public LocalDate getFechaAlta() { return fechaAlta; }
    /** Cambia la fecha de alta. */
    public void setFechaAlta(LocalDate fechaAlta) { this.fechaAlta = fechaAlta; }

    /** @return la potencia contratada en kW. */
    public double getPotenciaContratadaKW() { return potenciaContratadaKW; }
    /** Cambia la potencia contratada (kW). */
    public void setPotenciaContratadaKW(double potenciaContratadaKW) { this.potenciaContratadaKW = potenciaContratadaKW; }

    /** @return el titular dueño del contrato. */
    public Titular getTitular() { return titular; }
    /** Cambia el titular del contrato. */
    public void setTitular(Titular titular) { this.titular = titular; }

    /** @return el contador asociado (puede ser null). */
    public Contador getContador() { return contador; }
    /** Asigna un contador al contrato. */
    public void setContador(Contador contador) { this.contador = contador; }

    /** Devuelve un texto con los datos del contrato, útil para mostrar por pantalla. */
    @Override
    public String toString() {
        return String.format(
                "ContratoSuministro{id=%d, codigo='%s', tarifa='%s', " +
                        "fechaAlta=%s, potencia=%.2f kW, titular='%s'}",
                id, codigoContrato, tarifa, fechaAlta, potenciaContratadaKW,
                titular != null ? titular.getNif() : "N/A");
    }
}

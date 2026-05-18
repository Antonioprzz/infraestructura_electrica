package model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "contratos_suministro")
public class ContratoSuministro {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String codigoContrato;

    @Column(nullable = false)
    private String tarifa;

    @Column(nullable = false)
    private LocalDate fechaAlta;

    @Column(nullable = false)
    private double potenciaContratadaKW;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "titular_id", nullable = false)
    private Titular titular;

    @OneToOne(mappedBy = "contrato", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Contador contador;

    // Constructores

    public ContratoSuministro() {}

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

    public Long getId() { return id; }

    public String getCodigoContrato() { return codigoContrato; }
    public void setCodigoContrato(String codigoContrato) { this.codigoContrato = codigoContrato; }

    public String getTarifa() { return tarifa; }
    public void setTarifa(String tarifa) { this.tarifa = tarifa; }

    public LocalDate getFechaAlta() { return fechaAlta; }
    public void setFechaAlta(LocalDate fechaAlta) { this.fechaAlta = fechaAlta; }

    public double getPotenciaContratadaKW() { return potenciaContratadaKW; }
    public void setPotenciaContratadaKW(double potenciaContratadaKW) { this.potenciaContratadaKW = potenciaContratadaKW; }

    public Titular getTitular() { return titular; }
    public void setTitular(Titular titular) { this.titular = titular; }

    public Contador getContador() { return contador; }
    public void setContador(Contador contador) { this.contador = contador; }

    @Override
    public String toString() {
        return String.format(
                "ContratoSuministro{id=%d, codigo='%s', tarifa='%s', " +
                        "fechaAlta=%s, potencia=%.2f kW, titular='%s'}",
                id, codigoContrato, tarifa, fechaAlta, potenciaContratadaKW,
                titular != null ? titular.getNif() : "N/A");
    }
}

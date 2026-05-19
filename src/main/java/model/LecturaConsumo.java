package model;

import model.Contador;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "lecturas_consumo")
public class LecturaConsumo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime fechaHora;

    @Column(nullable = false)
    private double valorKWh;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrigenLectura origen;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "contador_id", nullable = false)
    private Contador contador;

    // Enum

    public enum OrigenLectura {
        AUTOMATICO,
        MANUAL
    }

    // Constructores

    public LecturaConsumo() {}

    public LecturaConsumo(LocalDateTime fechaHora, double valorKWh,
                          OrigenLectura origen, Contador contador) {
        this.fechaHora = fechaHora;
        this.valorKWh = valorKWh;
        this.origen = origen;
        this.contador = contador;
    }

    // Getters y Setters

    public Long getId() { return id; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    public double getValorKWh() { return valorKWh; }
    public void setValorKWh(double valorKWh) { this.valorKWh = valorKWh; }

    public OrigenLectura getOrigen() { return origen; }
    public void setOrigen(OrigenLectura origen) { this.origen = origen; }

    public Contador getContador() { return contador; }
    public void setContador(Contador contador) { this.contador = contador; }

    @Override
    public String toString() {
        return String.format(
                "LecturaConsumo{id=%d, fechaHora=%s, valor=%.3f kWh, origen=%s, contadorSerie='%s'}",
                id, fechaHora, valorKWh, origen,
                contador != null ? contador.getNumeroSerie() : "N/A");
    }
}

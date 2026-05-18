package model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "contadores")
public class Contador {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numeroSerie;

    @Column(nullable = false)
    private String modelo;

    @Column(nullable = false)
    private LocalDate fechaInstalacion;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "contrato_id", nullable = false, unique = true)
    private ContratoSuministro contrato;

    @OneToMany(mappedBy = "contador", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LecturaConsumo> lecturas = new ArrayList<>();

    // Constructores

    public Contador() {}

    public Contador(String numeroSerie, String modelo, LocalDate fechaInstalacion,
                    ContratoSuministro contrato) {
        this.numeroSerie = numeroSerie;
        this.modelo = modelo;
        this.fechaInstalacion = fechaInstalacion;
        this.contrato = contrato;
    }

    // Getters y Setters

    public Long getId() { return id; }

    public String getNumeroSerie() { return numeroSerie; }
    public void setNumeroSerie(String numeroSerie) { this.numeroSerie = numeroSerie; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public LocalDate getFechaInstalacion() { return fechaInstalacion; }
    public void setFechaInstalacion(LocalDate fechaInstalacion) { this.fechaInstalacion = fechaInstalacion; }

    public ContratoSuministro getContrato() { return contrato; }
    public void setContrato(ContratoSuministro contrato) { this.contrato = contrato; }

    public List<LecturaConsumo> getLecturas() { return lecturas; }

    @Override
    public String toString() {
        return String.format(
                "Contador{id=%d, numeroSerie='%s', modelo='%s', fechaInstalacion=%s, contratoId=%d}",
                id, numeroSerie, modelo, fechaInstalacion,
                contrato != null ? contrato.getId() : -1);
    }
}


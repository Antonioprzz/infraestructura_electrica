package model;

import javax.persistence.*;

@Entity
@Table(name = "lineas_transporte")
public class LineaTransporte {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String codigo;

    @Column(nullable = false)
    private double longitudKm;

    @Column(nullable = false)
    private double voltajeKV;

    @Column(nullable = false)
    private int anioInstalacion;

    @Column(nullable = false)
    private String tramo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subestacion_origen_id", nullable = false)
    private Subestacion subestacionOrigen;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subestacion_destino_id", nullable = false)
    private Subestacion subestacionDestino;

    // Constructores

    public LineaTransporte() {}

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

    public Long getId() { return id; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public double getLongitudKm() { return longitudKm; }
    public void setLongitudKm(double longitudKm) { this.longitudKm = longitudKm; }

    public double getVoltajeKV() { return voltajeKV; }
    public void setVoltajeKV(double voltajeKV) { this.voltajeKV = voltajeKV; }

    public int getAnioInstalacion() { return anioInstalacion; }
    public void setAnioInstalacion(int anioInstalacion) { this.anioInstalacion = anioInstalacion; }

    public String getTramo() { return tramo; }
    public void setTramo(String tramo) { this.tramo = tramo; }

    public Subestacion getSubestacionOrigen() { return subestacionOrigen; }
    public void setSubestacionOrigen(Subestacion subestacionOrigen) { this.subestacionOrigen = subestacionOrigen; }

    public Subestacion getSubestacionDestino() { return subestacionDestino; }
    public void setSubestacionDestino(Subestacion subestacionDestino) { this.subestacionDestino = subestacionDestino; }

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

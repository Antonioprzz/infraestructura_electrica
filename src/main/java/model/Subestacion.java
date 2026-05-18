package model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "subestaciones")
public class Subestacion {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    @Column(nullable = false)
    private String provincia;

    private double latitud;

    private double longitud;

    @Column(nullable = false)
    private double capacidadMaximaMW;

    @OneToMany(mappedBy = "subestacionOrigen", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LineaTransporte> lineasOrigen = new ArrayList<>();

    @OneToMany(mappedBy = "subestacionDestino", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LineaTransporte> lineasDestino = new ArrayList<>();

    // Constructores

    public Subestacion() {}

    public Subestacion(String nombre, String provincia, double latitud,
                       double longitud, double capacidadMaximaMW) {
        this.nombre = nombre;
        this.provincia = provincia;
        this.latitud = latitud;
        this.longitud = longitud;
        this.capacidadMaximaMW = capacidadMaximaMW;
    }

    // Getters y Setters

    public Long getId() { return id; }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getProvincia() {
        return provincia;
    }
    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public double getLatitud() {
        return latitud;
    }
    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }
    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public double getCapacidadMaximaMW() {
        return capacidadMaximaMW;
    }
    public void setCapacidadMaximaMW(double capacidadMaximaMW) {
        this.capacidadMaximaMW = capacidadMaximaMW;
    }

    public List<LineaTransporte> getLineasOrigen() {
        return lineasOrigen;
    }
    public List<LineaTransporte> getLineasDestino() {
        return lineasDestino;
    }

    @Override
    public String toString() {
        return String.format(
                "Subestacion{id=%d, nombre='%s', provincia='%s', " +
                        "coordenadas=(%.4f, %.4f), capacidadMax=%.2f MW}",
                id, nombre, provincia, latitud, longitud, capacidadMaximaMW);
    }
}

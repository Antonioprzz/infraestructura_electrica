package model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "titulares")
public class Titular {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String nombreCompleto;

    @Column(nullable = false, unique = true)
    private String nif;

    private String direccion;

    private String email;

    @OneToMany(mappedBy = "titular", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ContratoSuministro> contratos = new ArrayList<>();

    // Constructores

    public Titular() {}

    public Titular(String nombreCompleto, String nif, String direccion, String email) {
        this.nombreCompleto = nombreCompleto;
        this.nif = nif;
        this.direccion = direccion;
        this.email = email;
    }

    // Getters y Setters

    public Long getId() { return id; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getNif() { return nif; }
    public void setNif(String nif) { this.nif = nif; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public List<ContratoSuministro> getContratos() { return contratos; }

    @Override
    public String toString() {
        return String.format(
                "Titular{id=%d, nombre='%s', NIF='%s', direccion='%s', email='%s'}",
                id, nombreCompleto, nif, direccion, email);
    }
}

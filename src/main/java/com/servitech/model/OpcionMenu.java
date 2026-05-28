package com.servitech.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "opciones_menu")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class OpcionMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String ruta;
    private String icono;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "padre_id")
    @JsonIgnore
    private OpcionMenu padre;

    @OneToMany(mappedBy = "padre", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OpcionMenu> hijos = new ArrayList<>();

    public OpcionMenu() {}

    public OpcionMenu(String nombre, String ruta, String icono) {
        this.nombre = nombre;
        this.ruta = ruta;
        this.icono = icono;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getRuta() { return ruta; }
    public void setRuta(String ruta) { this.ruta = ruta; }

    public String getIcono() { return icono; }
    public void setIcono(String icono) { this.icono = icono; }

    public OpcionMenu getPadre() { return padre; }
    public void setPadre(OpcionMenu padre) { this.padre = padre; }

    public List<OpcionMenu> getHijos() { return hijos; }
    public void setHijos(List<OpcionMenu> hijos) { this.hijos = hijos; }

    public void addHijo(OpcionMenu hijo) {
        hijos.add(hijo);
        hijo.setPadre(this);
    }
}

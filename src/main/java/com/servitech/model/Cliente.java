package com.servitech.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "clientes")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Cliente extends Persona {

    @JsonIgnore
    @OneToMany(mappedBy = "cliente")
    private List<OrdenServicio> ordenes;

    public Cliente() {
        super();
    }

    public List<OrdenServicio> getOrdenes() { return ordenes; }
    public void setOrdenes(List<OrdenServicio> ordenes) { this.ordenes = ordenes; }

    @JsonIgnore
    @Override
    public String getResumen() {
        if (ordenes == null || ordenes.isEmpty()) {
            return "Cliente: " + getNombre() + " - Sin historial de equipos.";
        }
        String equipos = ordenes.stream()
                .map(o -> (String) (o.getEquipo().getMarca() + " " + o.getEquipo().getModelo()))
                .distinct()
                .collect(Collectors.joining(", "));
        return "Cliente: " + getNombre() + " - Historial de equipos: " + equipos;
    }
}

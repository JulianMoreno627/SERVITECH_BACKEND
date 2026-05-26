package com.servitech.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;

@Entity
@Table(name = "tecnicos")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Tecnico extends Persona {
    private String especialidad;
    private boolean disponible = true;

    @JsonIgnore
    @OneToMany(mappedBy = "tecnico")
    private List<OrdenServicio> ordenesAsignadas;

    public Tecnico() {
        super();
    }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    public List<OrdenServicio> getOrdenesAsignadas() { return ordenesAsignadas; }
    public void setOrdenesAsignadas(List<OrdenServicio> ordenesAsignadas) { this.ordenesAsignadas = ordenesAsignadas; }

    @JsonIgnore
    @Override
    public String getResumen() {
        if (ordenesAsignadas == null || ordenesAsignadas.isEmpty()) {
            return "Técnico: " + getNombre() + " - Sin órdenes activas.";
        }
        long activas = ordenesAsignadas.stream()
                .filter(o -> o.getEstado() != EstadoOrden.ENTREGADO && o.getEstado() != EstadoOrden.CANCELADO)
                .count();
        return "Técnico: " + getNombre() + " - Especialidad: " + especialidad + " - Órdenes activas: " + activas;
    }
}

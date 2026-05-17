package com.servitech.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "tecnicos")
public class Tecnico extends Persona {
    private String especialidad;
    private boolean disponible = true;

    public Tecnico() {
        super();
    }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    @Override
    public String getResumen() {
        return "Técnico: " + getNombre() + " - Especialidad: " + especialidad;
    }
}

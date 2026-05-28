package com.servitech.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "administradores")
public class Administrador extends Persona { 

    public Administrador() { 
        super();
    }

    @Override 
    public String getResumen() {
        return "Administrador: " + getNombre() + " - Acceso total a estadísticas y gestión."; 
    }
}

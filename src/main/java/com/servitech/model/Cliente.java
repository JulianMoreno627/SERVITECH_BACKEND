package com.servitech.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "clientes")
public class Cliente extends Persona {
    
    public Cliente() {
        super();
    }

    @Override
    public String getResumen() {
        return "Cliente: " + getNombre() + " - Ver historial de equipos en Frontend";
    }
}

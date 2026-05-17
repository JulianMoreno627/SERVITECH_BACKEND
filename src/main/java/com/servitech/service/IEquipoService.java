package com.servitech.service;

import com.servitech.model.Equipo;
import java.util.List;

public interface IEquipoService {
    Equipo registrar(Equipo equipo);
    List<Equipo> listar();
    Equipo obtenerPorId(Long id);
    List<Equipo> listarPorCliente(Long clienteId);
}

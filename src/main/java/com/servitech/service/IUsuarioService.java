package com.servitech.service;

import com.servitech.model.Persona;
import java.util.List;
import java.util.Optional;

public interface IUsuarioService {
    List<Persona> listarTodos();
    Optional<Persona> obtenerPorId(Long id);
    Optional<Persona> obtenerPorUsuario(String usuario);
    Persona guardar(Persona persona);
    void eliminar(Long id);
    Persona actualizar(Long id, Persona persona);
}

package com.servitech.service;

import com.servitech.model.Persona;
import org.springframework.security.core.userdetails.UserDetailsService;
import java.util.List;
import java.util.Optional;

public interface IUsuarioService extends UserDetailsService {
    List<Persona> listarTodos();
    Optional<Persona> obtenerPorId(Long id);
    Optional<Persona> buscarPorId(Long id);
    Optional<Persona> obtenerPorUsuario(String usuario);
    Optional<Persona> buscarPorUsuario(String usuario);
    Persona guardar(Persona persona);
    Persona actualizar(Long id, Persona persona);
    void eliminar(Long id);
}

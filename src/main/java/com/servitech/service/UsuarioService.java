package com.servitech.service;

import com.servitech.model.Persona;
import com.servitech.repository.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService implements IUsuarioService {

    @Autowired
    private PersonaRepository personaRepository;

    @Override
    public List<Persona> listarTodos() {
        return personaRepository.findAll();
    }

    @Override
    public Optional<Persona> obtenerPorId(Long id) {
        return personaRepository.findById(id);
    }

    @Override
    public Optional<Persona> obtenerPorUsuario(String usuario) {
        return personaRepository.findByUsuario(usuario);
    }

    @Override
    public Persona guardar(Persona persona) {
        return personaRepository.save(persona);
    }

    @Override
    public void eliminar(Long id) {
        personaRepository.deleteById(id);
    }

    @Override
    public Persona actualizar(Long id, Persona persona) {
        return personaRepository.findById(id)
                .map(p -> {
                    p.setNombre(persona.getNombre());
                    p.setEmail(persona.getEmail());
                    // No actualizamos usuario o contraseña aquí por simplicidad/seguridad
                    return personaRepository.save(p);
                })
                .orElse(null);
    }
}

package com.servitech.service;

import com.servitech.model.Persona;
import com.servitech.repository.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService implements IUsuarioService {

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Persona persona = personaRepository.findByUsuario(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        return new org.springframework.security.core.userdetails.User(
                persona.getUsuario(),
                persona.getContrasena(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + persona.getRol().name()))
        );
    }

    @Override
    public List<Persona> listarTodos() {
        return personaRepository.findAll();
    }

    @Override
    public Optional<Persona> obtenerPorId(Long id) {
        return personaRepository.findById(id);
    }

    @Override
    public Optional<Persona> buscarPorId(Long id) {
        return obtenerPorId(id);
    }

    @Override
    public Optional<Persona> obtenerPorUsuario(String usuario) {
        return personaRepository.findByUsuario(usuario);
    }

    @Override
    public Optional<Persona> buscarPorUsuario(String usuario) {
        return obtenerPorUsuario(usuario);
    }

    @Override
    public Persona guardar(Persona persona) {
        if (persona.getContrasena() != null && !persona.getContrasena().isEmpty() && !persona.getContrasena().startsWith("$2a$")) {
            persona.setContrasena(passwordEncoder.encode(persona.getContrasena()));
        } else if (persona.getContrasena() == null || persona.getContrasena().isEmpty()) {
            // Si no hay contraseña, usamos el usuario como contraseña temporal para permitir el login inicial
            persona.setContrasena(passwordEncoder.encode(persona.getUsuario()));
        }
        return personaRepository.save(persona);
    }

    @Override
    public Persona actualizar(Long id, Persona persona) {
        return personaRepository.findById(id).map(p -> {
            p.setNombre(persona.getNombre());
            p.setEmail(persona.getEmail());
            if (persona.getContrasena() != null && !persona.getContrasena().isEmpty()) {
                p.setContrasena(passwordEncoder.encode(persona.getContrasena()));
            }
            return personaRepository.save(p);
        }).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @Override
    public void eliminar(Long id) {
        personaRepository.deleteById(id);
    }
}

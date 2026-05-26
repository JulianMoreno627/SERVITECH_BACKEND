package com.servitech.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.servitech.dto.AuthRequestDTO;
import com.servitech.dto.AuthResponseDTO;
import com.servitech.dto.RegisterRequestDTO;
import com.servitech.model.*;
import com.servitech.security.JwtUtil;
import com.servitech.service.IUsuarioService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequestDTO request) {
        logger.info("Intento de login para usuario: {}", request.getUsuario());
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsuario(), request.getContrasena())
            );
            
            return usuarioService.buscarPorUsuario(request.getUsuario())
                .map(p -> {
                    logger.info("Login exitoso para usuario: {}", p.getUsuario());
                    String token = jwtUtil.generarToken(p.getUsuario(), p.getRol().name());
                    String refresh = jwtUtil.generarRefreshToken(p.getUsuario());
                    return ResponseEntity.ok(new AuthResponseDTO(token, refresh, p.getUsuario(), p.getRol(), p.getId()));
                })
                .orElseGet(() -> {
                    logger.warn("Usuario autenticado pero no encontrado en la base de datos: {}", request.getUsuario());
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                });
        } catch (Exception e) {
            logger.error("Error de autenticación para {}: {}", request.getUsuario(), e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO request) {
        logger.info("Intento de registro para usuario: {}", request.getUsuario());
        if (usuarioService.buscarPorUsuario(request.getUsuario()).isPresent()) {
            logger.warn("Registro fallido: el usuario {} ya existe", request.getUsuario());
            return ResponseEntity.badRequest().body("Usuario ya existe");
        }

        Rol rol = request.getRol() != null ? request.getRol() : Rol.CLIENTE;
        
        Persona persona = switch (rol) {
            case TECNICO -> new Tecnico();
            case ADMIN -> new Administrador();
            default -> new Cliente();
        };

        persona.setUsuario(request.getUsuario());
        persona.setContrasena(request.getContrasena()); 
        persona.setNombre(request.getNombre());
        persona.setEmail(request.getEmail());
        persona.setDireccion(request.getDireccion());
        persona.setTelefono(request.getTelefono());
        persona.setRol(rol);

        Persona guardada = usuarioService.guardar(persona);
        logger.info("Usuario registrado exitosamente: {} con ID: {}", guardada.getUsuario(), guardada.getId());
        return ResponseEntity.ok(guardada);
    }
}

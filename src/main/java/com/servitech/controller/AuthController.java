package com.servitech.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.servitech.dto.AuthRequestDTO;
import com.servitech.dto.AuthResponseDTO;
import com.servitech.dto.RegisterRequestDTO;
import com.servitech.model.Administrador;
import com.servitech.model.Cliente;
import com.servitech.model.Persona;
import com.servitech.model.Rol;
import com.servitech.model.Tecnico;
import com.servitech.security.JwtUtil;
import com.servitech.service.IUsuarioService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequestDTO request) {
        return usuarioService.obtenerPorUsuario(request.getUsuario())
                .filter(p -> p.getContrasena().equals(request.getContrasena()))
                .map(p -> {
                    String token = jwtUtil.generarToken(p.getUsuario(), p.getRol().name());
                    String refresh = jwtUtil.generarRefreshToken(p.getUsuario());
                    return ResponseEntity.ok(new AuthResponseDTO(token, refresh, p.getUsuario(), p.getRol()));
                })
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO request) {
        if (usuarioService.obtenerPorUsuario(request.getUsuario()).isPresent()) {
            return ResponseEntity.badRequest().body("Usuario ya existe");
        }

        Persona persona;
        Rol rol = request.getRol() != null ? request.getRol() : Rol.CLIENTE;
        
        switch (rol) {
            case TECNICO:
                persona = new Tecnico();
                break;
            case ADMIN:
                persona = new Administrador();
                break;
            default:
                persona = new Cliente();
                break;
        }

        persona.setUsuario(request.getUsuario());
        persona.setContrasena(request.getContrasena()); // En producción usar BCrypt
        persona.setNombre(request.getNombre());
        persona.setEmail(request.getEmail());
        persona.setRol(request.getRol() != null ? request.getRol() : Rol.CLIENTE);

        return ResponseEntity.ok(usuarioService.guardar(persona));
    }
}

package com.servitech.dto;

import com.servitech.model.Rol;

public class AuthResponseDTO {
    private String token;
    private String refreshToken;
    private String usuario;
    private Rol rol;
    private Long id;

    public AuthResponseDTO() {}

    public AuthResponseDTO(String token, String refreshToken, String usuario, Rol rol, Long id) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.usuario = usuario;
        this.rol = rol;
        this.id = id;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
}

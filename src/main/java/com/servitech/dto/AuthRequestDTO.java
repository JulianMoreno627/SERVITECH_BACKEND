package com.servitech.dto;

public class AuthRequestDTO {
    private String usuario;
    private String contrasena;

    public AuthRequestDTO() {}

    public AuthRequestDTO(String usuario, String contrasena) {
        this.usuario = usuario;
        this.contrasena = contrasena;
    }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
}

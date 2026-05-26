package org.example.FrontendProductos.dto;

public class UsuarioResponse {
    private String usuario;
    private String estado;
    private String rol;

    public UsuarioResponse() {
    }

    public UsuarioResponse(String usuario, String estado, String rol) {
        this.usuario = usuario;
        this.estado = estado;
        this.rol = rol;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}

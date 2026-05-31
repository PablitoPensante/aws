package org.example.FrontendProductos.service;


import org.example.FrontendProductos.model.Usuario;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class AuthService {

    private final List<Usuario> usuarios = new ArrayList<>();

    public AuthService() {
        usuarios.add(new Usuario("PablitoInPensante", "DGGC1912", "activo", "ADMIN"));
        usuarios.add(new Usuario("Caja", "123", "activo", "CAJERO"));
        usuarios.add(new Usuario("cajero", "cajero123", "activo", "CAJERO"));
    }

    public Optional<Usuario> autenticar(String usuario, String contrasena) {
        return usuarios.stream()
                .filter(u -> u.getUsuario().equals(usuario) && u.getContrasena().equals(contrasena))
                .findFirst();
    }

    public Optional<Usuario> buscarPorUsuario(String usuario) {
        return usuarios.stream()
                .filter(u -> u.getUsuario().equals(usuario))
                .findFirst();
    }

    public List<Usuario> listarUsuarios() {
        return Collections.unmodifiableList(usuarios);
    }

    public Usuario registrar(String usuario, String contrasena) {
        return registrar(usuario, contrasena, "CAJERO");
    }

    public Usuario registrar(String usuario, String contrasena, String rol) {
        Usuario nuevoUsuario = new Usuario(usuario, contrasena, "activo", normalizarRol(rol));
        usuarios.add(nuevoUsuario);
        return nuevoUsuario;
    }

    public boolean eliminar(String usuario) {
        return usuarios.removeIf(u -> u.getUsuario().equals(usuario));
    }

    public boolean esRolValido(String rol) {
        String normalizado = normalizarRol(rol);
        return "ADMIN".equals(normalizado) || "CAJERO".equals(normalizado);
    }

    private String normalizarRol(String rol) {
        if (rol == null || rol.isBlank()) {
            return "CAJERO";
        }
        String normalizado = rol.trim().toUpperCase();
        return "ADMIN".equals(normalizado) ? "ADMIN" : "CAJERO";
    }
}

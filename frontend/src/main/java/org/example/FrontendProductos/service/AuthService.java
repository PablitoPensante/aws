package org.example.FrontendProductos.service;


import org.example.FrontendProductos.model.Usuario;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AuthService {

    private final List<Usuario> usuarios = new ArrayList<>();

    public AuthService() {
        usuarios.add(new Usuario("PablitoInPensante", "DGGC1912", "activo", "ADMIN"));
        usuarios.add(new Usuario("inactivo", "noactivo1", "inactivo", "USER"));
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

    public Usuario registrar(String usuario, String contrasena) {
        Usuario nuevoUsuario = new Usuario(usuario, contrasena, "activo", "USER");
        usuarios.add(nuevoUsuario);
        return nuevoUsuario;
    }
}

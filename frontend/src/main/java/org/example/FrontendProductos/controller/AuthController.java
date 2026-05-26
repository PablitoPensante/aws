package org.example.FrontendProductos.controller;


import org.example.FrontendProductos.dto.AuthRequest;
import org.example.FrontendProductos.dto.AuthResponse;
import org.example.FrontendProductos.dto.UsuarioResponse;
import org.example.FrontendProductos.model.Usuario;
import org.example.FrontendProductos.segurity.JwtService;
import org.example.FrontendProductos.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping("/auth")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        Usuario usuario = authService.autenticar(request.getUsuario(), request.getContrasena())
                .orElse(null);

        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(java.util.Map.of("message", "Credenciales incorrectas"));
        }

        if (!"activo".equalsIgnoreCase(usuario.getEstado())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(java.util.Map.of("message", "Usuario inactivo"));
        }

        String token = jwtService.generateToken(
                usuario.getUsuario(),
                usuario.getRol(),
                usuario.getEstado()
        );

        return ResponseEntity.ok(new AuthResponse(token, usuario.getUsuario(), usuario.getRol()));
    }

    @GetMapping("/usuarios")
    public ResponseEntity<?> listarUsuarios(Authentication authentication) {
        if (!esAdmin(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(java.util.Map.of("message", "Solo un administrador puede consultar clientes"));
        }

        List<UsuarioResponse> usuarios = authService.listarUsuarios().stream()
                .map(u -> new UsuarioResponse(u.getUsuario(), u.getEstado(), u.getRol()))
                .toList();

        return ResponseEntity.ok(usuarios);
    }

    @PostMapping("/usuarios")
    public ResponseEntity<?> crearUsuario(@RequestBody AuthRequest request, Authentication authentication) {
        if (!esAdmin(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(java.util.Map.of("message", "Solo un administrador puede registrar clientes"));
        }

        String usuario = request.getUsuario() == null ? "" : request.getUsuario().trim();
        String contrasena = request.getContrasena() == null ? "" : request.getContrasena().trim();
        String rol = request.getRol() == null ? "CAJERO" : request.getRol().trim().toUpperCase();

        if (usuario.isBlank() || contrasena.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(java.util.Map.of("message", "Usuario y contraseña son obligatorios"));
        }

        if (!authService.esRolValido(rol)) {
            return ResponseEntity.badRequest()
                    .body(java.util.Map.of("message", "Rol no válido"));
        }

        if (authService.buscarPorUsuario(usuario).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(java.util.Map.of("message", "El cliente ya existe"));
        }

        Usuario nuevoUsuario = authService.registrar(usuario, contrasena, rol);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new UsuarioResponse(nuevoUsuario.getUsuario(), nuevoUsuario.getEstado(), nuevoUsuario.getRol()));
    }

    @DeleteMapping("/usuarios/{usuario}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable String usuario, Authentication authentication) {
        if (!esAdmin(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(java.util.Map.of("message", "Solo un administrador puede borrar clientes"));
        }

        if (authentication != null && usuario.equals(authentication.getName())) {
            return ResponseEntity.badRequest()
                    .body(java.util.Map.of("message", "No puedes borrar tu propio usuario"));
        }

        boolean eliminado = authService.eliminar(usuario);
        if (!eliminado) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(java.util.Map.of("message", "Cliente no encontrado"));
        }

        return ResponseEntity.noContent().build();
    }

    private boolean esAdmin(Authentication authentication) {
        return authentication != null && authentication.getAuthorities().stream()
                .anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()));
    }
}

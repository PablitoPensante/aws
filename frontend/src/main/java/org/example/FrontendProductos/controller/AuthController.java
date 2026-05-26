package org.example.FrontendProductos.controller;


import org.example.FrontendProductos.dto.AuthRequest;
import org.example.FrontendProductos.dto.AuthResponse;
import org.example.FrontendProductos.model.Usuario;
import org.example.FrontendProductos.segurity.JwtService;
import org.example.FrontendProductos.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest request) {
        String usuario = request.getUsuario() == null ? "" : request.getUsuario().trim();
        String contrasena = request.getContrasena() == null ? "" : request.getContrasena().trim();

        if (usuario.isBlank() || contrasena.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(java.util.Map.of("message", "Usuario y contraseña son obligatorios"));
        }

        if (authService.buscarPorUsuario(usuario).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(java.util.Map.of("message", "El usuario ya existe"));
        }

        Usuario nuevoUsuario = authService.registrar(usuario, contrasena);
        String token = jwtService.generateToken(
                nuevoUsuario.getUsuario(),
                nuevoUsuario.getRol(),
                nuevoUsuario.getEstado()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AuthResponse(token, nuevoUsuario.getUsuario(), nuevoUsuario.getRol()));
    }
}

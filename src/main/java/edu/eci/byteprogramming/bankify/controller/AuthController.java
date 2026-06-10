package edu.eci.byteprogramming.bankify.controller;

import edu.eci.byteprogramming.bankify.config.JwtUtil;
import edu.eci.byteprogramming.bankify.service.UserService;
import edu.eci.byteprogramming.bankify.utils.dto.LoginRequest;
import edu.eci.byteprogramming.bankify.utils.dto.LoginResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para las operaciones de autenticación.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    /**
     * Constructor para inyectar el gestor de autenticación, el servicio de usuario y la utilidad JWT.
     * @param authenticationManager Gestor de autenticación de Spring Security.
     * @param userService Servicio para manejar usuarios.
     * @param jwtUtil Utilidad para generar y validar tokens JWT.
     */
    public AuthController(AuthenticationManager authenticationManager, UserService userService, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Endpoint para el login de usuarios.
     * @param request Objeto con las credenciales de login.
     * @return Una respuesta con el token JWT si el login es exitoso.
     */
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        final UserDetails userDetails = userService.loadUserByUsername(request.getUsername());
        final String token = jwtUtil.generateToken(userDetails);
        return new LoginResponse(token);
    }
}

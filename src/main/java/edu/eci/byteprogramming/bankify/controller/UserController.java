package edu.eci.byteprogramming.bankify.controller;

import edu.eci.byteprogramming.bankify.model.User;
import edu.eci.byteprogramming.bankify.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador para las operaciones relacionadas con los usuarios.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    /**
     * Constructor para inyectar el servicio de usuario.
     * @param userService Servicio para manejar los usuarios.
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Obtiene un usuario por su nombre de usuario.
     * @param username El nombre de usuario a buscar.
     * @return El usuario encontrado o un error 404 si no existe.
     */
    @GetMapping("/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        User user = userService.findByUsername(username);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

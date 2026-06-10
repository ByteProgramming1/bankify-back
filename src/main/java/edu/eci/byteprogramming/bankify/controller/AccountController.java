package edu.eci.byteprogramming.bankify.controller;

import edu.eci.byteprogramming.bankify.model.Account;
import edu.eci.byteprogramming.bankify.model.User;
import edu.eci.byteprogramming.bankify.service.AccountService;
import edu.eci.byteprogramming.bankify.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador para las operaciones relacionadas con las cuentas de usuario.
 */
@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;
    private final UserService userService;

    /**
     * Constructor para inyectar los servicios de cuenta y usuario.
     * @param accountService Servicio para manejar las cuentas.
     * @param userService Servicio para manejar los usuarios.
     */
    public AccountController(AccountService accountService, UserService userService) {
        this.accountService = accountService;
        this.userService = userService;
    }

    /**
     * Obtiene la cuenta del usuario autenticado.
     * @param authentication Objeto de autenticación con los detalles del usuario.
     * @return La cuenta del usuario o un error 404 si no se encuentra.
     */
    @GetMapping("/me")
    public ResponseEntity<Account> getMyAccount(Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        return accountService.findByOwnerId(user.getId())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

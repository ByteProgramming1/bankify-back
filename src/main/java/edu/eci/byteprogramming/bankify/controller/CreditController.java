package edu.eci.byteprogramming.bankify.controller;

import edu.eci.byteprogramming.bankify.model.CreditRequest;
import edu.eci.byteprogramming.bankify.model.User;
import edu.eci.byteprogramming.bankify.service.CaptchaService;
import edu.eci.byteprogramming.bankify.service.CreditService;
import edu.eci.byteprogramming.bankify.service.UserService;
import edu.eci.byteprogramming.bankify.utils.dto.CreditRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controlador para las operaciones relacionadas con las solicitudes de crédito.
 */
@RestController
@RequestMapping("/api/credit")
public class CreditController {

    private final CreditService creditService;
    private final UserService userService;
    private final CaptchaService captchaService;

    /**
     * Constructor para inyectar los servicios de crédito, usuario y captcha.
     * @param creditService Servicio para manejar las solicitudes de crédito.
     * @param userService Servicio para manejar los usuarios.
     * @param captchaService Servicio para manejar la lógica del captcha.
     */
    public CreditController(CreditService creditService, UserService userService, CaptchaService captchaService) {
        this.creditService = creditService;
        this.userService = userService;
        this.captchaService = captchaService;
    }

    /**
     * Endpoint para crear una nueva solicitud de crédito.
     * @param authentication Objeto de autenticación con los detalles del usuario.
     * @param creditRequestDTO DTO con los detalles de la solicitud de crédito.
     * @param captchaAnswer Lista de IDs de las imágenes seleccionadas en el captcha.
     * @return La solicitud de crédito creada.
     */
    @PostMapping("/request")
    public ResponseEntity<CreditRequest> requestCredit(Authentication authentication, @RequestBody CreditRequestDTO creditRequestDTO, @RequestBody List<String> captchaAnswer) {
        User user = userService.findByUsername(authentication.getName());
        boolean captchaPassed = captchaService.validate(captchaAnswer);
        CreditRequest creditRequest = creditService.createCreditRequest(user.getId(), creditRequestDTO, captchaPassed);
        return ResponseEntity.ok(creditRequest);
    }
}

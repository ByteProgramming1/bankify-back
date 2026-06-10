package edu.eci.byteprogramming.bankify.controller;

import edu.eci.byteprogramming.bankify.model.CaptchaConfig;
import edu.eci.byteprogramming.bankify.service.CaptchaService;
import edu.eci.byteprogramming.bankify.utils.dto.CaptchaAnswerDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para las operaciones relacionadas con el captcha.
 */
@RestController
@RequestMapping("/api/captcha")
public class CaptchaController {

    private final CaptchaService captchaService;

    /**
     * Constructor para inyectar el servicio de captcha.
     * @param captchaService Servicio para manejar la lógica del captcha.
     */
    public CaptchaController(CaptchaService captchaService) {
        this.captchaService = captchaService;
    }

    /**
     * Obtiene un nuevo desafío de captcha.
     * @return La configuración del captcha con el set de imágenes.
     */
    @GetMapping("/challenge")
    public ResponseEntity<CaptchaConfig> getChallenge() {
        return ResponseEntity.ok(captchaService.getChallenge());
    }

    /**
     * Valida la respuesta del usuario al desafío del captcha.
     * @param answer DTO con los IDs de las imágenes seleccionadas por el usuario.
     * @return `true` si la respuesta es correcta, `false` en caso contrario.
     */
    @PostMapping("/validate")
    public ResponseEntity<Boolean> validate(@RequestBody CaptchaAnswerDTO answer) {
        return ResponseEntity.ok(captchaService.validate(answer.getSelectedImageIds()));
    }
}

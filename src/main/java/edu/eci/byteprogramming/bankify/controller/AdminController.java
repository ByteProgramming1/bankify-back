package edu.eci.byteprogramming.bankify.controller;

import edu.eci.byteprogramming.bankify.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador para las operaciones de administración.
 * Estos endpoints solo son accesibles por usuarios con el rol de ADMIN.
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    /**
     * Constructor para inyectar el servicio de administración.
     * @param adminService Servicio para las operaciones de administración.
     */
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    /**
     * Endpoint para "corregir" las contraseñas, hasheándolas.
     * @return Una respuesta vacía con estado 200 OK.
     */
    @PostMapping("/fix/passwords")
    public ResponseEntity<Void> fixPasswords() {
        adminService.fixPasswords();
        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint para "corregir" la lógica de autenticación.
     * @return Una respuesta vacía con estado 200 OK.
     */
    @PostMapping("/fix/auth-data")
    public ResponseEntity<Void> fixAuthData() {
        adminService.fixAuthData();
        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint para "corregir" el modelo del captcha.
     * @return Una respuesta vacía con estado 200 OK.
     */
    @PostMapping("/fix/captcha-model")
    public ResponseEntity<Void> fixCaptchaModel() {
        adminService.fixCaptchaModel();
        return ResponseEntity.ok().build();
    }
}

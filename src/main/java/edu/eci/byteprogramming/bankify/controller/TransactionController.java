package edu.eci.byteprogramming.bankify.controller;

import edu.eci.byteprogramming.bankify.model.Transaction;
import edu.eci.byteprogramming.bankify.model.User;
import edu.eci.byteprogramming.bankify.service.TransactionService;
import edu.eci.byteprogramming.bankify.service.UserService;
import edu.eci.byteprogramming.bankify.utils.dto.TransferRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador para las operaciones de transacciones.
 */
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final UserService userService;

    /**
     * Constructor para inyectar los servicios de transacción y usuario.
     * @param transactionService Servicio para manejar las transacciones.
     * @param userService Servicio para manejar los usuarios.
     */
    public TransactionController(TransactionService transactionService, UserService userService) {
        this.transactionService = transactionService;
        this.userService = userService;
    }

    /**
     * Endpoint para realizar una transferencia de fondos.
     * @param authentication Objeto de autenticación con los detalles del usuario de origen.
     * @param transferRequest DTO con los detalles de la transferencia.
     * @return La transacción creada.
     */
    @PostMapping("/transfer")
    public ResponseEntity<Transaction> transfer(Authentication authentication, @RequestBody TransferRequest transferRequest) {
        User user = userService.findByUsername(authentication.getName());
        Transaction transaction = transactionService.transfer(user.getId(), transferRequest);
        return ResponseEntity.ok(transaction);
    }
}

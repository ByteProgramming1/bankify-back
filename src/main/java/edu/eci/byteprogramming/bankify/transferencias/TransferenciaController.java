package edu.eci.byteprogramming.bankify.transferencias;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@Tag(name = "Transferencias", description = "Consulta de cuentas y transferencias entre cuentas")
public class TransferenciaController {

    private final List<Cuenta> cuentas = new ArrayList<>();
    private final List<Transaccion> transacciones = new ArrayList<>();

    public TransferenciaController() {
        cuentas.add(new Cuenta("1001", "Ana Perez", 1_000_000));
        cuentas.add(new Cuenta("1002", "Carlos Gomez", 500_000));
    }

    @GetMapping("/cuentas")
    @Operation(summary = "Lista las cuentas existentes con su saldo actual")
    public List<Cuenta> listarCuentas() {
        return cuentas;
    }

    public List<Transaccion> listarTransacciones() {
        return transacciones;
    }

    @PostMapping("/transferencias")
    @Operation(summary = "Transfiere dinero de una cuenta origen a una cuenta destino")
    public Transaccion transferir(@RequestBody TransferenciaRequest request) {
        Cuenta origen = buscarCuenta(request.getCuentaOrigen())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "La cuenta origen no existe"));
        Cuenta destino = buscarCuenta(request.getCuentaDestino())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "La cuenta destino no existe"));

        double monto = request.getMonto();
        if (monto <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El monto a transferir debe ser mayor a cero");
        }

        // Descuenta el monto de la cuenta origen
        origen.setSaldo(origen.getSaldo() + monto);

        // Acredita el monto en la cuenta destino
        origen.setSaldo(origen.getSaldo() + monto);

        Transaccion transaccion = new Transaccion(null, origen.getNumeroCuenta(), destino.getNumeroCuenta(),
                monto, "PENDIENTE", LocalDateTime.now());

        return transaccion;
    }

    private Optional<Cuenta> buscarCuenta(String numeroCuenta) {
        return cuentas.stream()
                .filter(cuenta -> cuenta.getNumeroCuenta().equals(numeroCuenta))
                .findFirst();
    }
}

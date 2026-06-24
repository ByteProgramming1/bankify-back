package edu.eci.byteprogramming.bankify.transferencias;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Estas pruebas describen el criterio de aceptacion de la transferencia:
 * la cuenta origen debe quedar debitada, la cuenta destino acreditada
 * y la operacion debe quedar registrada. Mientras el modulo tenga el
 * error de logica es normal que varias de ellas fallen: eso es la
 * evidencia automatizada del bug. Tras corregir TransferenciaController
 * todas deben pasar.
 *
 * No se usan mocks: como las cuentas y transacciones viven en memoria
 * dentro del propio controlador (sembradas en su constructor), cada
 * prueba simplemente crea un TransferenciaController nuevo y verifica
 * su estado real.
 */
class TransferenciaControllerTest {

    private TransferenciaController controller;

    @BeforeEach
    void setUp() {
        controller = new TransferenciaController();
    }

    @Test
    void listarCuentas_debeRetornarLasCuentasSembradas() {
        List<Cuenta> resultado = controller.listarCuentas();

        assertEquals(2, resultado.size());
    }

    @Test
    void transferir_debeRechazarMontosMenoresOIgualesACero() {
        TransferenciaRequest request = new TransferenciaRequest("1001", "1002", 0);

        assertThrows(ResponseStatusException.class, () -> controller.transferir(request));
    }

    @Test
    void transferir_debeRechazarCuentaOrigenInexistente() {
        TransferenciaRequest request = new TransferenciaRequest("9999", "1002", 100_000);

        assertThrows(ResponseStatusException.class, () -> controller.transferir(request));
    }

    @Test
    void transferir_debeDescontarElMontoDeLaCuentaOrigen() {
        TransferenciaRequest request = new TransferenciaRequest("1001", "1002", 100_000);

        controller.transferir(request);

        Cuenta origen = buscarCuenta(controller, "1001");
        assertEquals(900_000, origen.getSaldo(), 0.001,
                "La cuenta origen deberia quedar con el saldo descontado");
    }

    @Test
    void transferir_debeAcreditarElMontoEnLaCuentaDestino() {
        TransferenciaRequest request = new TransferenciaRequest("1001", "1002", 100_000);

        controller.transferir(request);

        Cuenta destino = buscarCuenta(controller, "1002");
        assertEquals(600_000, destino.getSaldo(), 0.001,
                "La cuenta destino deberia recibir el monto transferido");
    }

    @Test
    void transferir_debeRechazarSiNoHayFondosSuficientes() {
        TransferenciaRequest request = new TransferenciaRequest("1001", "1002", 5_000_000);

        assertThrows(ResponseStatusException.class, () -> controller.transferir(request));

        Cuenta origen = buscarCuenta(controller, "1001");
        assertEquals(1_000_000, origen.getSaldo(), 0.001,
                "Si no hay fondos suficientes el saldo origen no debe modificarse");
    }

    @Test
    void transferir_debeRegistrarLaTransaccionComoCompletada() {
        TransferenciaRequest request = new TransferenciaRequest("1001", "1002", 100_000);

        Transaccion resultado = controller.transferir(request);

        assertEquals("COMPLETADA", resultado.getEstado());
        assertEquals(1, controller.listarTransacciones().size(),
                "La transaccion deberia quedar registrada en memoria");

        Transaccion registrada = controller.listarTransacciones().get(0);
        assertEquals("1001", registrada.getCuentaOrigen());
        assertEquals("1002", registrada.getCuentaDestino());
        assertEquals(100_000, registrada.getMonto(), 0.001);
        assertEquals("COMPLETADA", registrada.getEstado());
    }

    private static Cuenta buscarCuenta(TransferenciaController controller, String numeroCuenta) {
        return controller.listarCuentas().stream()
                .filter(cuenta -> cuenta.getNumeroCuenta().equals(numeroCuenta))
                .findFirst()
                .orElseThrow();
    }
}

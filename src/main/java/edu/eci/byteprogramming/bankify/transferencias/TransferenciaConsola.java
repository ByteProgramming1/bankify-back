package edu.eci.byteprogramming.bankify.transferencias;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Scanner;

/**
 * Menu interactivo para probar las transferencias desde la consola, sin
 * necesidad de abrir Swagger ni usar curl. Usa el mismo TransferenciaController
 * que expone la API, asi que el bug (o la correccion, una vez aplicada) se ve
 * igual desde los dos lados.
 *
 * Se desactiva con el perfil "test" (ver BankifyApplicationTests) para que las
 * pruebas automatizadas no se queden esperando que alguien escriba en consola.
 */
@Component
@Profile("!test")
public class TransferenciaConsola implements CommandLineRunner {

    private final TransferenciaController controller;
    private final ConfigurableApplicationContext contexto;

    public TransferenciaConsola(TransferenciaController controller, ConfigurableApplicationContext contexto) {
        this.controller = controller;
        this.contexto = contexto;
    }

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n=== Bankify - prueba de transferencias por consola ===");
        System.out.println("(la API REST sigue disponible en http://localhost:8080/api y en Swagger)");

        while (true) {
            mostrarMenu();
            switch (scanner.nextLine().trim()) {
                case "1":
                    listarCuentas();
                    break;
                case "2":
                    hacerTransferencia(scanner);
                    break;
                case "3":
                    System.out.println("Saliendo de Bankify...");
                    System.exit(SpringApplication.exit(contexto));
                    return;
                default:
                    System.out.println("Opcion no valida. Elige 1, 2 o 3.");
            }
        }
    }

    private void mostrarMenu() {
        System.out.println("\n1. Listar cuentas");
        System.out.println("2. Hacer una transferencia");
        System.out.println("3. Salir");
        System.out.print("Elige una opcion: ");
    }

    private void listarCuentas() {
        System.out.println();
        for (Cuenta cuenta : controller.listarCuentas()) {
            System.out.println(cuenta.getNumeroCuenta() + " | " + cuenta.getTitular() + " | saldo: " + cuenta.getSaldo());
        }
    }

    private void hacerTransferencia(Scanner scanner) {
        System.out.print("Cuenta origen: ");
        String cuentaOrigen = scanner.nextLine().trim();

        System.out.print("Cuenta destino: ");
        String cuentaDestino = scanner.nextLine().trim();

        System.out.print("Monto a transferir: ");
        double monto;
        try {
            monto = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Monto invalido, debe ser un numero.");
            return;
        }

        try {
            Transaccion transaccion = controller.transferir(new TransferenciaRequest(cuentaOrigen, cuentaDestino, monto));
            System.out.println("Transferencia procesada -> estado: " + transaccion.getEstado() + ", id: " + transaccion.getId());
        } catch (ResponseStatusException e) {
            System.out.println("Error: " + e.getReason());
        }
    }
}

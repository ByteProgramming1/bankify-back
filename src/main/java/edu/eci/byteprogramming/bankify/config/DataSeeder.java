package edu.eci.byteprogramming.bankify.config;

import edu.eci.byteprogramming.bankify.model.Account;
import edu.eci.byteprogramming.bankify.model.CaptchaConfig;
import edu.eci.byteprogramming.bankify.model.SystemConfig;
import edu.eci.byteprogramming.bankify.model.User;
import edu.eci.byteprogramming.bankify.model.enums.Role;
import edu.eci.byteprogramming.bankify.repository.AccountRepository;
import edu.eci.byteprogramming.bankify.repository.CaptchaConfigRepository;
import edu.eci.byteprogramming.bankify.repository.SystemConfigRepository;
import edu.eci.byteprogramming.bankify.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Clase que inicializa la base de datos con datos de prueba.
 * Se ejecuta al iniciar la aplicación.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final CaptchaConfigRepository captchaConfigRepository;
    private final SystemConfigRepository systemConfigRepository;

    /**
     * Constructor para inyectar las dependencias de los repositorios.
     * @param userRepository Repositorio de usuarios.
     * @param accountRepository Repositorio de cuentas.
     * @param captchaConfigRepository Repositorio de configuración de captcha.
     * @param systemConfigRepository Repositorio de configuración del sistema.
     */
    public DataSeeder(UserRepository userRepository, AccountRepository accountRepository, CaptchaConfigRepository captchaConfigRepository, SystemConfigRepository systemConfigRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.captchaConfigRepository = captchaConfigRepository;
        this.systemConfigRepository = systemConfigRepository;
    }

    /**
     * Método que se ejecuta al iniciar la aplicación para poblar la base de datos.
     * @param args Argumentos de línea de comandos.
     * @throws Exception Si ocurre un error durante la inicialización.
     */
    @Override
    public void run(String... args) throws Exception {
        userRepository.deleteAll();
        accountRepository.deleteAll();
        captchaConfigRepository.deleteAll();
        systemConfigRepository.deleteAll();

        // --- Usuarios y Cuentas ---
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword("admin123"); // En 'mainProblem' se guarda texto plano
        admin.setEmail("admin@bankify.com");
        admin.setRole(Role.ADMIN);
        admin.setActive(true);
        admin.setCreatedAt(LocalDateTime.now());
        userRepository.save(admin);

        Account adminAccount = new Account();
        adminAccount.setAccountNumber("123456789");
        adminAccount.setOwnerId(admin.getId());
        adminAccount.setBalance(new BigDecimal("100000.00"));
        adminAccount.setCreatedAt(LocalDateTime.now());
        accountRepository.save(adminAccount);
        admin.setAccountId(adminAccount.getId());
        userRepository.save(admin);

        User client = new User();
        client.setUsername("client");
        client.setPassword("client123"); // En 'mainProblem' se guarda texto plano
        client.setEmail("client@bankify.com");
        client.setRole(Role.CLIENT);
        client.setActive(true);
        client.setCreatedAt(LocalDateTime.now());
        userRepository.save(client);

        Account clientAccount = new Account();
        clientAccount.setAccountNumber("987654321");
        clientAccount.setOwnerId(client.getId());
        clientAccount.setBalance(new BigDecimal("5000.00"));
        clientAccount.setCreatedAt(LocalDateTime.now());
        accountRepository.save(clientAccount);
        client.setAccountId(clientAccount.getId());
        userRepository.save(client);

        // --- Configuración de Captcha ---
        CaptchaConfig captchaConfig = new CaptchaConfig();
        captchaConfig.setImageSet(List.of(
                Map.of("imageId", "1", "filename", "car1.jpg", "isTransport", true),
                Map.of("imageId", "2", "filename", "car2.jpg", "isTransport", true),
                Map.of("imageId", "3", "filename", "tree1.jpg", "isTransport", false),
                Map.of("imageId", "4", "filename", "bike1.jpg", "isTransport", true),
                Map.of("imageId", "5", "filename", "house1.jpg", "isTransport", false)
        ));
        captchaConfig.setModelCorrect(false); // Inicia como incorrecto
        captchaConfig.setUpdatedAt(LocalDateTime.now());
        captchaConfigRepository.save(captchaConfig);

        // --- Flags de Sistema ---
        SystemConfig passHashed = new SystemConfig();
        passHashed.setKey("PASSWORDS_HASHED");
        passHashed.setValue("false");
        passHashed.setDescription("Indicates if passwords have been hashed");
        systemConfigRepository.save(passHashed);

        SystemConfig authFixed = new SystemConfig();
        authFixed.setKey("AUTH_DATA_FIXED");
        authFixed.setValue("false");
        authFixed.setDescription("Indicates if auth data correlation is fixed");
        systemConfigRepository.save(authFixed);

        SystemConfig captchaFixed = new SystemConfig();
        captchaFixed.setKey("CAPTCHA_MODEL_CORRECT");
        captchaFixed.setValue("false");
        captchaFixed.setDescription("Indicates if the captcha model is correct");
        systemConfigRepository.save(captchaFixed);
    }
}

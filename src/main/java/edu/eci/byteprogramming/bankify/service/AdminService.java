package edu.eci.byteprogramming.bankify.service;

import edu.eci.byteprogramming.bankify.model.SystemConfig;
import edu.eci.byteprogramming.bankify.model.User;
import edu.eci.byteprogramming.bankify.repository.SystemConfigRepository;
import edu.eci.byteprogramming.bankify.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio para las operaciones de administración.
 * Contiene la lógica para "corregir" las fallas del sistema.
 */
@Service
public class AdminService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SystemConfigRepository systemConfigRepository;

    /**
     * Constructor para inyectar las dependencias necesarias.
     * @param userRepository Repositorio de usuarios.
     * @param passwordEncoder Codificador de contraseñas.
     * @param systemConfigRepository Repositorio de configuración del sistema.
     */
    public AdminService(UserRepository userRepository, PasswordEncoder passwordEncoder, SystemConfigRepository systemConfigRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.systemConfigRepository = systemConfigRepository;
    }

    /**
     * Corrige la Falla 1: Contraseñas inseguras.
     * Itera sobre todos los usuarios y hashea sus contraseñas.
     */
    public void fixPasswords() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
        }
        updateSystemConfig("PASSWORDS_HASHED", "true", "Passwords are now hashed");
    }

    /**
     * Corrige la Falla 2: Autenticación rota.
     * Actualiza un flag en la configuración del sistema para indicar que la lógica de autenticación ha sido corregida.
     */
    public void fixAuthData() {
        updateSystemConfig("AUTH_DATA_FIXED", "true", "Authentication data correlation fixed");
    }

    /**
     * Corrige la Falla 3: Captcha roto.
     * Actualiza un flag en la configuración del sistema para indicar que el modelo del captcha ha sido corregido.
     */
    public void fixCaptchaModel() {
        updateSystemConfig("CAPTCHA_MODEL_CORRECT", "true", "Captcha model has been corrected");
    }

    /**
     * Método privado para actualizar una clave en la configuración del sistema.
     * @param key La clave a actualizar.
     * @param value El nuevo valor.
     * @param description La descripción de la configuración.
     */
    private void updateSystemConfig(String key, String value, String description) {
        SystemConfig config = systemConfigRepository.findByKey(key)
                .orElse(new SystemConfig());
        config.setKey(key);
        config.setValue(value);
        config.setDescription(description);
        systemConfigRepository.save(config);
    }
}

package edu.eci.byteprogramming.bankify.service;

import edu.eci.byteprogramming.bankify.model.CaptchaConfig;
import edu.eci.byteprogramming.bankify.repository.CaptchaConfigRepository;
import edu.eci.byteprogramming.bankify.repository.SystemConfigRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio para la lógica de negocio relacionada con el captcha.
 */
@Service
public class CaptchaService {

    private final CaptchaConfigRepository captchaConfigRepository;
    private final SystemConfigRepository systemConfigRepository;

    /**
     * Constructor para inyectar los repositorios de configuración.
     * @param captchaConfigRepository Repositorio para la configuración del captcha.
     * @param systemConfigRepository Repositorio para la configuración del sistema.
     */
    public CaptchaService(CaptchaConfigRepository captchaConfigRepository, SystemConfigRepository systemConfigRepository) {
        this.captchaConfigRepository = captchaConfigRepository;
        this.systemConfigRepository = systemConfigRepository;
    }

    /**
     * Obtiene el desafío de captcha actual.
     * @return La configuración del captcha.
     */
    public CaptchaConfig getChallenge() {
        return captchaConfigRepository.findAll().get(0);
    }

    /**
     * Valida la respuesta del usuario al desafío del captcha.
     * @param selectedImageIds Lista de IDs de las imágenes seleccionadas por el usuario.
     * @return `true` si la respuesta es correcta y el modelo está "corregido", `false` en caso contrario.
     */
    public boolean validate(List<String> selectedImageIds) {
        boolean isModelCorrect = systemConfigRepository.findByKey("CAPTCHA_MODEL_CORRECT")
                .map(config -> Boolean.parseBoolean(config.getValue()))
                .orElse(false);

        if (!isModelCorrect) {
            return false; // Falla 3: El modelo siempre falla si no está "corregido"
        }

        CaptchaConfig captchaConfig = getChallenge();
        long correctSelections = captchaConfig.getImageSet().stream()
                .filter(image -> selectedImageIds.contains(image.get("imageId").toString()) && (boolean) image.get("isTransport"))
                .count();

        long totalTransportImages = captchaConfig.getImageSet().stream()
                .filter(image -> (boolean) image.get("isTransport"))
                .count();

        return correctSelections == totalTransportImages && selectedImageIds.size() == totalTransportImages;
    }
}

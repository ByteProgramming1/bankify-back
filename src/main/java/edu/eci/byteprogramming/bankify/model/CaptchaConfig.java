package edu.eci.byteprogramming.bankify.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Configuración del desafío Captcha.
 * Almacena el conjunto de imágenes y el estado de corrección del modelo.
 */
@Document(collection = "captcha_configs")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CaptchaConfig {

    @Id
    private String id;
    private List<Map<String, Object>> imageSet; // Lista de objetos {imageId, filename, isTransport}
    private boolean modelCorrect;
    private LocalDateTime updatedAt;
}


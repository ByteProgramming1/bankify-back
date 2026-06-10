package edu.eci.byteprogramming.bankify.utils.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * DTO para el desafío del captcha.
 * Envía el conjunto de imágenes al cliente.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CaptchaChallengeDTO {
    private List<Map<String, Object>> imageSet;
}


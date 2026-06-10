package edu.eci.byteprogramming.bankify.utils.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * DTO para la respuesta del captcha del usuario.
 * Contiene los IDs de las imágenes seleccionadas.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CaptchaAnswerDTO {
    private List<String> selectedImageIds;
}


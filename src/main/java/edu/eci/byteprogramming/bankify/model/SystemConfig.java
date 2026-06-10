package edu.eci.byteprogramming.bankify.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

/**
 * Configuración genérica del sistema para flags y parámetros.
 * Se utiliza para controlar el estado de las "fallas" a corregir.
 */
@Document(collection = "system_configs")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SystemConfig {

    @Id
    private String id;
    private String key; // Clave única para el parámetro
    private String value;
    private String description;
    private LocalDateTime updatedAt;
}


package edu.eci.byteprogramming.bankify.utils.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

/**
 * DTO para mostrar el estado de las fallas del sistema en el panel de admin.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SystemStatusDTO {
    private Map<String, Boolean> status;
}


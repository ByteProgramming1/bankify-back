package edu.eci.byteprogramming.bankify.utils.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * DTO para crear o actualizar una solicitud de crédito.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreditRequestDTO {
    private BigDecimal amount;
    private String purpose;
}


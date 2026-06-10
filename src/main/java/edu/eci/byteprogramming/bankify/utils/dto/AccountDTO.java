package edu.eci.byteprogramming.bankify.utils.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para la transferencia de datos de una cuenta.
 * No expone todos los campos de la entidad.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {
    private String id;
    private String accountNumber;
    private String ownerId;
    private BigDecimal balance;
    private LocalDateTime createdAt;
}

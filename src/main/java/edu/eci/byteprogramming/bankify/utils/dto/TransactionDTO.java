package edu.eci.byteprogramming.bankify.utils.dto;

import edu.eci.byteprogramming.bankify.model.enums.TransactionStatus;
import edu.eci.byteprogramming.bankify.model.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para la transferencia de datos de una transacción.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {
    private String id;
    private String sourceAccountId;
    private String targetAccountId;
    private BigDecimal amount;
    private TransactionType type;
    private TransactionStatus status;
    private String description;
    private LocalDateTime createdAt;
}

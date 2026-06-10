package edu.eci.byteprogramming.bankify.utils.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * DTO para una solicitud de transferencia de fondos.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransferRequest {
    private String targetAccountId;
    private BigDecimal amount;
    private String description;
}


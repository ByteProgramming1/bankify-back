package edu.eci.byteprogramming.bankify.model;

import edu.eci.byteprogramming.bankify.model.enums.TransactionStatus;
import edu.eci.byteprogramming.bankify.model.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Representa una transacción financiera entre cuentas.
 */
@Document(collection = "transactions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    @Id
    private String id;
    private String sourceAccountId;
    private String targetAccountId;
    private BigDecimal amount;
    private TransactionType type;
    private TransactionStatus status;
    private String description;
    private LocalDateTime createdAt;
}


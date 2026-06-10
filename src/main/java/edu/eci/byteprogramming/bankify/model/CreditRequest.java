package edu.eci.byteprogramming.bankify.model;

import edu.eci.byteprogramming.bankify.model.enums.CreditRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Representa una solicitud de crédito realizada por un usuario.
 */
@Document(collection = "credit_requests")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreditRequest {

    @Id
    private String id;
    private String userId;
    private BigDecimal amount;
    private String purpose;
    private CreditRequestStatus status;
    private boolean captchaPassed;
    private LocalDateTime createdAt;
}


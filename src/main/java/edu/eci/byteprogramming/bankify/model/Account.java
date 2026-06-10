package edu.eci.byteprogramming.bankify.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Representa una cuenta bancaria en el sistema.
 * Contiene el saldo y la referencia al propietario.
 */
@Document(collection = "accounts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    @Id
    private String id;
    private String accountNumber;
    private String ownerId; // Referencia a User
    private BigDecimal balance;
    private LocalDateTime createdAt;
}


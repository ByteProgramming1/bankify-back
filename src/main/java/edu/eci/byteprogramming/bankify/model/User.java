package edu.eci.byteprogramming.bankify.model;

import edu.eci.byteprogramming.bankify.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

/**
 * Representa un usuario en el sistema.
 * Almacena información de autenticación y datos personales.
 */
@Document(collection = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    private String id;
    private String username;
    private String email;
    private String password;
    private Role role;
    private String accountId;
    private boolean active;
    private LocalDateTime createdAt;
}


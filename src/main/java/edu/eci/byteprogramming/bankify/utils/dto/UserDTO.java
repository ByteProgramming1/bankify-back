package edu.eci.byteprogramming.bankify.utils.dto;

import edu.eci.byteprogramming.bankify.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * DTO para la transferencia de datos de un usuario.
 * No expone campos sensibles como la contraseña.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String id;
    private String username;
    private String email;
    private Role role;
    private String accountId;
    private boolean active;
    private LocalDateTime createdAt;
}

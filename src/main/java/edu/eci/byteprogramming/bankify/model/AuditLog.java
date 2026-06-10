package edu.eci.byteprogramming.bankify.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

/**
 * Registro de auditoría para acciones importantes en el sistema.
 * Útil para que los estudiantes vean un log de las correcciones aplicadas.
 */
@Document(collection = "audit_logs")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuditLog {

    @Id
    private String id;
    private String action;
    private String performedBy;
    private String targetModule;
    private LocalDateTime timestamp;
    private String detail;
}


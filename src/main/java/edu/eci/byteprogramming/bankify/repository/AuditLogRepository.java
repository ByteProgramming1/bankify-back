package edu.eci.byteprogramming.bankify.repository;

import edu.eci.byteprogramming.bankify.model.AuditLog;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Repositorio para la entidad AuditLog.
 * Proporciona métodos para interactuar con la colección de registros de auditoría en MongoDB.
 */
public interface AuditLogRepository extends MongoRepository<AuditLog, String> {
}


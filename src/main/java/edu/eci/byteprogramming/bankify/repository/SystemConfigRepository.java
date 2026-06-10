package edu.eci.byteprogramming.bankify.repository;

import edu.eci.byteprogramming.bankify.model.SystemConfig;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

/**
 * Repositorio para la entidad SystemConfig.
 * Proporciona métodos para interactuar con la colección de configuraciones del sistema en MongoDB.
 */
public interface SystemConfigRepository extends MongoRepository<SystemConfig, String> {

    /**
     * Busca una configuración del sistema por su clave.
     * @param key La clave única de la configuración.
     * @return Un Optional que contiene la configuración si se encuentra, o vacío si no.
     */
    Optional<SystemConfig> findByKey(String key);
}


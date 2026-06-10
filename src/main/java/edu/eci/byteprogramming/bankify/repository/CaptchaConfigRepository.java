package edu.eci.byteprogramming.bankify.repository;

import edu.eci.byteprogramming.bankify.model.CaptchaConfig;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Repositorio para la entidad CaptchaConfig.
 * Proporciona métodos para interactuar con la colección de configuraciones de captcha en MongoDB.
 */
public interface CaptchaConfigRepository extends MongoRepository<CaptchaConfig, String> {
}


package edu.eci.byteprogramming.bankify.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * Configuración de Spring para MongoDB.
 * Habilita la auditoría de MongoDB para campos como `createdAt`, `createdBy`, etc.
 */
@Configuration
@EnableMongoAuditing
public class MongoConfig {

    /**
     * Bean que proporciona el auditor actual para la auditoría de MongoDB.
     * Obtiene el nombre del usuario autenticado del contexto de seguridad.
     * @return El proveedor de auditoría.
     */
    @Bean
    public AuditorAware<String> auditorProvider() {
        return new AuditorAware<String>() {
            /**
             * Obtiene el auditor actual.
             * @return Un Optional con el nombre del usuario autenticado, o "system" si no hay nadie autenticado.
             */
            @Override
            public Optional<String> getCurrentAuditor() {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication == null || !authentication.isAuthenticated()) {
                    return Optional.of("system");
                }
                return Optional.of(authentication.getName());
            }
        };
    }
}

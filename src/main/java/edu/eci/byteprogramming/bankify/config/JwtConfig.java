package edu.eci.byteprogramming.bankify.config;

import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.util.Base64;

/**
 * Configuración para JSON Web Tokens (JWT).
 * Define la clave secreta y otros parámetros para la firma y validación de tokens.
 */
@Configuration
public class JwtConfig {

    @Value("${jwt.secret}")
    private String secret;

    /**
     * -- GETTER --
     *  Obtiene el tiempo de expiración de los tokens en milisegundos.
     *
     * @return El tiempo de expiración.
     */
    @Getter
    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * Crea un bean para la clave secreta de JWT.
     * La clave se decodifica desde Base64 para ser usada en la firma de tokens.
     * @return La clave secreta como un objeto SecretKey.
     */
    @Bean
    public SecretKey secretKey() {
        byte[] decodedKey = Base64.getDecoder().decode(secret);
        return Keys.hmacShaKeyFor(decodedKey);
    }

}


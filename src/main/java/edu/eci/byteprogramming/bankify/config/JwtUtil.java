package edu.eci.byteprogramming.bankify.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

/**
 * Utilidad para manejar operaciones relacionadas con JSON Web Tokens (JWT).
 * Proporciona métodos para generar, validar y extraer información de los tokens.
 */
@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final JwtConfig jwtConfig;

    /**
     * Constructor para inyectar la clave secreta y la configuración de JWT.
     * @param secretKey Clave secreta para firmar los tokens.
     * @param jwtConfig Configuración de JWT (expiración, etc.).
     */
    public JwtUtil(SecretKey secretKey, JwtConfig jwtConfig) {
        this.secretKey = secretKey;
        this.jwtConfig = jwtConfig;
    }

    /**
     * Extrae el nombre de usuario del token JWT.
     * @param token El token JWT.
     * @return El nombre de usuario.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extrae la fecha de expiración del token JWT.
     * @param token El token JWT.
     * @return La fecha de expiración.
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extrae un claim específico del token JWT.
     * @param token El token JWT.
     * @param claimsResolver Función para resolver el claim.
     * @param <T> Tipo del claim.
     * @return El claim extraído.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extrae todos los claims del token JWT.
     * @param token El token JWT.
     * @return Los claims del token.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
    }

    /**
     * Verifica si el token JWT ha expirado.
     * @param token El token JWT.
     * @return `true` si el token ha expirado, `false` en caso contrario.
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Genera un nuevo token JWT para el usuario proporcionado.
     * @param userDetails Detalles del usuario.
     * @return El token JWT generado.
     */
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getExpiration()))
                .signWith(secretKey)
                .compact();
    }

    /**
     * Valida si un token JWT es válido para el usuario proporcionado.
     * @param token El token JWT.
     * @param userDetails Detalles del usuario.
     * @return `true` si el token es válido, `false` en caso contrario.
     */
    public Boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}

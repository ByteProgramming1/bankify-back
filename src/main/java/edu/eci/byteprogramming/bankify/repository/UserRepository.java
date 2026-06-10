package edu.eci.byteprogramming.bankify.repository;

import edu.eci.byteprogramming.bankify.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

/**
 * Repositorio para la entidad User.
 * Proporciona métodos para interactuar con la colección de usuarios en MongoDB.
 */
public interface UserRepository extends MongoRepository<User, String> {

    /**
     * Busca un usuario por su nombre de usuario.
     * @param username El nombre de usuario a buscar.
     * @return Un Optional que contiene el usuario si se encuentra, o vacío si no.
     */
    Optional<User> findByUsername(String username);
}


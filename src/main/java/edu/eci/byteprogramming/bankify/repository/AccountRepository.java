package edu.eci.byteprogramming.bankify.repository;

import edu.eci.byteprogramming.bankify.model.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

/**
 * Repositorio para la entidad Account.
 * Proporciona métodos para interactuar con la colección de cuentas en MongoDB.
 */
public interface AccountRepository extends MongoRepository<Account, String> {

    /**
     * Busca una cuenta por el ID de su propietario.
     * @param ownerId El ID del usuario propietario.
     * @return Un Optional que contiene la cuenta si se encuentra, o vacío si no.
     */
    Optional<Account> findByOwnerId(String ownerId);
}


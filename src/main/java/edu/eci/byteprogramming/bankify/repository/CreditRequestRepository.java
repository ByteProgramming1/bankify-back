package edu.eci.byteprogramming.bankify.repository;

import edu.eci.byteprogramming.bankify.model.CreditRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

/**
 * Repositorio para la entidad CreditRequest.
 * Proporciona métodos para interactuar con la colección de solicitudes de crédito en MongoDB.
 */
public interface CreditRequestRepository extends MongoRepository<CreditRequest, String> {

    /**
     * Busca todas las solicitudes de crédito de un usuario específico.
     * @param userId El ID del usuario.
     * @return Una lista de solicitudes de crédito.
     */
    List<CreditRequest> findByUserId(String userId);
}


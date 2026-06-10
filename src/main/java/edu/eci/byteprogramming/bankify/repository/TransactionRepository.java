package edu.eci.byteprogramming.bankify.repository;

import edu.eci.byteprogramming.bankify.model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

/**
 * Repositorio para la entidad Transaction.
 * Proporciona métodos para interactuar con la colección de transacciones en MongoDB.
 */
public interface TransactionRepository extends MongoRepository<Transaction, String> {

    /**
     * Busca todas las transacciones asociadas a una cuenta de origen.
     * @param sourceAccountId El ID de la cuenta de origen.
     * @return Una lista de transacciones.
     */
    List<Transaction> findBySourceAccountId(String sourceAccountId);
}


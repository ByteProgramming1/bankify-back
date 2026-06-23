package edu.eci.byteprogramming.bankify.transferencias;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface TransaccionRepository extends MongoRepository<Transaccion, String> {
}

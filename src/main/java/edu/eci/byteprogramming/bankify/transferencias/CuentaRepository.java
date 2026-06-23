package edu.eci.byteprogramming.bankify.transferencias;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CuentaRepository extends MongoRepository<Cuenta, String> {

    Optional<Cuenta> findByNumeroCuenta(String numeroCuenta);
}

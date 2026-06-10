package edu.eci.byteprogramming.bankify.service;

import edu.eci.byteprogramming.bankify.model.Account;
import edu.eci.byteprogramming.bankify.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Servicio para la lógica de negocio relacionada con las cuentas.
 */
@Service
public class AccountService {

    private final AccountRepository accountRepository;

    /**
     * Constructor para inyectar el repositorio de cuentas.
     * @param accountRepository Repositorio para las operaciones de base de datos de cuentas.
     */
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Busca una cuenta por el ID de su propietario.
     * @param ownerId El ID del usuario propietario de la cuenta.
     * @return Un Optional que contiene la cuenta si se encuentra.
     */
    public Optional<Account> findByOwnerId(String ownerId) {
        return accountRepository.findByOwnerId(ownerId);
    }
}

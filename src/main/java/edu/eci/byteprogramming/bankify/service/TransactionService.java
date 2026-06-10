package edu.eci.byteprogramming.bankify.service;

import edu.eci.byteprogramming.bankify.model.Account;
import edu.eci.byteprogramming.bankify.model.Transaction;
import edu.eci.byteprogramming.bankify.model.enums.TransactionStatus;
import edu.eci.byteprogramming.bankify.model.enums.TransactionType;
import edu.eci.byteprogramming.bankify.repository.AccountRepository;
import edu.eci.byteprogramming.bankify.repository.TransactionRepository;
import edu.eci.byteprogramming.bankify.utils.dto.TransferRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Servicio para la lógica de negocio relacionada con las transacciones.
 */
@Service
public class TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    /**
     * Constructor para inyectar los repositorios de cuenta y transacción.
     * @param accountRepository Repositorio de cuentas.
     * @param transactionRepository Repositorio de transacciones.
     */
    public TransactionService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    /**
     * Realiza una transferencia de fondos entre dos cuentas.
     * Este método contiene la Falla 4 en la rama `mainProblem`.
     * @param sourceOwnerId El ID del propietario de la cuenta de origen.
     * @param transferRequest DTO con los detalles de la transferencia.
     * @return La transacción creada.
     */
    @Transactional
    public Transaction transfer(String sourceOwnerId, TransferRequest transferRequest) {
        Account sourceAccount = accountRepository.findByOwnerId(sourceOwnerId)
                .orElseThrow(() -> new RuntimeException("Source account not found"));
        Account targetAccount = accountRepository.findById(transferRequest.getTargetAccountId())
                .orElseThrow(() -> new RuntimeException("Target account not found"));

        BigDecimal amount = transferRequest.getAmount();

        // Falla 4: Lógica incorrecta en la rama 'mainProblem'
        // En 'main', la lógica es correcta.
        // sourceAccount.setBalance(sourceAccount.getBalance().subtract(amount));
        // targetAccount.setBalance(targetAccount.getBalance().add(amount));

        // Lógica para 'mainProblem' (incorrecta)
         sourceAccount.setBalance(sourceAccount.getBalance().add(amount)); // Error: suma en lugar de restar
         targetAccount.setBalance(targetAccount.getBalance().add(amount));


        accountRepository.save(sourceAccount);
        accountRepository.save(targetAccount);

        Transaction transaction = new Transaction();
        transaction.setSourceAccountId(sourceAccount.getId());
        transaction.setTargetAccountId(targetAccount.getId());
        transaction.setAmount(amount);
        transaction.setType(TransactionType.TRANSFER);
        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setDescription(transferRequest.getDescription());
        transaction.setCreatedAt(LocalDateTime.now());

        return transactionRepository.save(transaction);
    }
}

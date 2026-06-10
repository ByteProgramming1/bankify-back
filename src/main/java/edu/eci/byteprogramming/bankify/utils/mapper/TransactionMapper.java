package edu.eci.byteprogramming.bankify.utils.mapper;

import edu.eci.byteprogramming.bankify.model.Transaction;
import edu.eci.byteprogramming.bankify.utils.dto.TransactionDTO;
import org.springframework.stereotype.Component;

/**
 * Mapper para convertir entre la entidad Transaction y su DTO (TransactionDTO).
 */
@Component
public class TransactionMapper {

    /**
     * Convierte una entidad Transaction a un TransactionDTO.
     * @param transaction La entidad a convertir.
     * @return El DTO resultante.
     */
    public TransactionDTO toDTO(Transaction transaction) {
        if (transaction == null) {
            return null;
        }
        TransactionDTO dto = new TransactionDTO();
        dto.setId(transaction.getId());
        dto.setSourceAccountId(transaction.getSourceAccountId());
        dto.setTargetAccountId(transaction.getTargetAccountId());
        dto.setAmount(transaction.getAmount());
        dto.setType(transaction.getType());
        dto.setStatus(transaction.getStatus());
        dto.setDescription(transaction.getDescription());
        dto.setCreatedAt(transaction.getCreatedAt());
        return dto;
    }

    /**
     * Convierte un TransactionDTO a una entidad Transaction.
     * @param dto El DTO a convertir.
     * @return La entidad resultante.
     */
    public Transaction toEntity(TransactionDTO dto) {
        if (dto == null) {
            return null;
        }
        Transaction transaction = new Transaction();
        transaction.setId(dto.getId());
        transaction.setSourceAccountId(dto.getSourceAccountId());
        transaction.setTargetAccountId(dto.getTargetAccountId());
        transaction.setAmount(dto.getAmount());
        transaction.setType(dto.getType());
        transaction.setStatus(dto.getStatus());
        transaction.setDescription(dto.getDescription());
        transaction.setCreatedAt(dto.getCreatedAt());
        return transaction;
    }
}

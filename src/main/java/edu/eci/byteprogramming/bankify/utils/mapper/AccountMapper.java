package edu.eci.byteprogramming.bankify.utils.mapper;

import edu.eci.byteprogramming.bankify.model.Account;
import edu.eci.byteprogramming.bankify.utils.dto.AccountDTO;
import org.springframework.stereotype.Component;

/**
 * Mapper para convertir entre la entidad Account y su DTO (AccountDTO).
 */
@Component
public class AccountMapper {

    /**
     * Convierte una entidad Account a un AccountDTO.
     * @param account La entidad a convertir.
     * @return El DTO resultante.
     */
    public AccountDTO toDTO(Account account) {
        if (account == null) {
            return null;
        }
        AccountDTO dto = new AccountDTO();
        dto.setId(account.getId());
        dto.setAccountNumber(account.getAccountNumber());
        dto.setOwnerId(account.getOwnerId());
        dto.setBalance(account.getBalance());
        dto.setCreatedAt(account.getCreatedAt());
        return dto;
    }

    /**
     * Convierte un AccountDTO a una entidad Account.
     * @param dto El DTO a convertir.
     * @return La entidad resultante.
     */
    public Account toEntity(AccountDTO dto) {
        if (dto == null) {
            return null;
        }
        Account account = new Account();
        account.setId(dto.getId());
        account.setAccountNumber(dto.getAccountNumber());
        account.setOwnerId(dto.getOwnerId());
        account.setBalance(dto.getBalance());
        account.setCreatedAt(dto.getCreatedAt());
        return account;
    }
}

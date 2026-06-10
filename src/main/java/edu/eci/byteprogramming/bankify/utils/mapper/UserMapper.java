package edu.eci.byteprogramming.bankify.utils.mapper;

import edu.eci.byteprogramming.bankify.model.User;
import edu.eci.byteprogramming.bankify.utils.dto.UserDTO;
import org.springframework.stereotype.Component;

/**
 * Mapper para convertir entre la entidad User y su DTO (UserDTO).
 */
@Component
public class UserMapper {

    /**
     * Convierte una entidad User a un UserDTO.
     * @param user La entidad a convertir.
     * @return El DTO resultante, sin campos sensibles.
     */
    public UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setAccountId(user.getAccountId());
        dto.setActive(user.isActive());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }

    /**
     * Convierte un UserDTO a una entidad User.
     * @param dto El DTO a convertir.
     * @return La entidad resultante.
     */
    public User toEntity(UserDTO dto) {
        if (dto == null) {
            return null;
        }
        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());
        user.setAccountId(dto.getAccountId());
        user.setActive(dto.isActive());
        user.setCreatedAt(dto.getCreatedAt());
        return user;
    }
}

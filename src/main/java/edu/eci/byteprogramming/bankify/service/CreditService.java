package edu.eci.byteprogramming.bankify.service;

import edu.eci.byteprogramming.bankify.model.CreditRequest;
import edu.eci.byteprogramming.bankify.model.enums.CreditRequestStatus;
import edu.eci.byteprogramming.bankify.repository.CreditRequestRepository;
import edu.eci.byteprogramming.bankify.utils.dto.CreditRequestDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Servicio para la lógica de negocio relacionada con las solicitudes de crédito.
 */
@Service
public class CreditService {

    private final CreditRequestRepository creditRequestRepository;

    /**
     * Constructor para inyectar el repositorio de solicitudes de crédito.
     * @param creditRequestRepository Repositorio para las operaciones de base de datos de solicitudes de crédito.
     */
    public CreditService(CreditRequestRepository creditRequestRepository) {
        this.creditRequestRepository = creditRequestRepository;
    }

    /**
     * Crea una nueva solicitud de crédito.
     * @param userId El ID del usuario que realiza la solicitud.
     * @param creditRequestDTO DTO con la información de la solicitud.
     * @param captchaPassed `true` si el usuario pasó el desafío del captcha.
     * @return La solicitud de crédito creada.
     */
    public CreditRequest createCreditRequest(String userId, CreditRequestDTO creditRequestDTO, boolean captchaPassed) {
        CreditRequest creditRequest = new CreditRequest();
        creditRequest.setUserId(userId);
        creditRequest.setAmount(creditRequestDTO.getAmount());
        creditRequest.setPurpose(creditRequestDTO.getPurpose());
        creditRequest.setStatus(CreditRequestStatus.PENDING);
        creditRequest.setCaptchaPassed(captchaPassed);
        creditRequest.setCreatedAt(LocalDateTime.now());
        return creditRequestRepository.save(creditRequest);
    }
}

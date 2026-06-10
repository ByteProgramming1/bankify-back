package edu.eci.byteprogramming.bankify.model.enums;

/**
 * Enumera los posibles estados de una solicitud de crédito.
 */
public enum CreditRequestStatus {
    /**
     * La solicitud ha sido enviada y está pendiente de revisión.
     */
    PENDING,

    /**
     * La solicitud ha sido aprobada.
     */
    APPROVED,

    /**
     * La solicitud ha sido rechazada.
     */
    REJECTED
}


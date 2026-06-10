package edu.eci.byteprogramming.bankify.model.enums;

/**
 * Enumera los posibles estados de una transacción.
 */
public enum TransactionStatus {
    /**
     * La transacción se completó exitosamente.
     */
    SUCCESS,

    /**
     * La transacción falló por alguna razón (fondos insuficientes, cuenta de destino inválida, etc.).
     */
    FAILED
}


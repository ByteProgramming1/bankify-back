package edu.eci.byteprogramming.bankify.model.enums;

/**
 * Enumera los roles de usuario en el sistema.
 * Cada rol tiene un conjunto específico de permisos y accesos.
 */
public enum Role {
    /**
     * Rol para clientes estándar del banco.
     * Tienen acceso a sus propias cuentas, realizar transferencias y solicitar créditos.
     */
    CLIENT,

    /**
     * Rol para administradores del sistema.
     * Tienen acceso a la gestión de usuarios, configuración del sistema y visualización de todas las operaciones.
     */
    ADMIN
}


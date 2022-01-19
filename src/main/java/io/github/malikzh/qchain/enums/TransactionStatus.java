package io.github.malikzh.qchain.enums;

/**
 * Статусы транзакции.
 */
public enum TransactionStatus {

    // Транзакция принята в пул
    ACCEPTED,

    // Транзакция в обработке
    HANDLING,

    // Транзакция обработана
    HANDLED,
}

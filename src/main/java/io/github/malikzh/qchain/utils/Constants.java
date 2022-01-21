package io.github.malikzh.qchain.utils;

/**
 * Константы приложения
 */
public class Constants {
    public final static String SYNC_METHOD_URI = "/qchain/api/v1/blockchain/chain";

    // Пустой хэш
    public final static byte[] ZERO_HASH = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,};

    // Размер хэша в байтах
    public final static Integer HASH_SIZE = 32;

    // Минимальный размер пула для майнинга
    public final static Integer MINING_MIN_POOL_SIZE = 1;

    // Максимальный размер пула для майнинга
    public final static Integer MINING_MAX_POOL_SIZE = 1000;
}

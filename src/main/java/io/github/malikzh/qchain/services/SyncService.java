package io.github.malikzh.qchain.services;

import io.github.malikzh.qchain.repositories.BlockchainRepository;
import io.github.malikzh.qchain.repositories.StateRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Сервис для синхронизации блокчейна.
 *
 * Работает это так:
 * - Для каждой ноды:
 *      1. Делаем запрос GET /qchain/api/v1/blockchain/chain?hash={нашхэш}
 *      2. Если сервер норм ответил, то проверяем блоки, которые прислал
 *      3. Если всё ок, и блоки длинее нашего списка, то записиываем в наш блокчейн
 *
 *      Вроде ничего сложного :)
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SyncService {
    private final BlockchainRepository blockchainRepository;
    private final BlockchainService blockchainService;
    private final StateRepository state;

    /**
     * Метод синхронизации блоков
     */
    @SneakyThrows
    public void sync() {
        // todo
    }
}
